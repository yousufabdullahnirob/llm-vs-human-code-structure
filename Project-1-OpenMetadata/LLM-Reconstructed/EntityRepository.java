package com.example.metadata.repository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Generic base class for persisting a single entity type. Concrete
 * repositories (e.g. UserRepository) extend this and supply the DAO plus a
 * few hooks; this class handles the common create/update/delete flow and,
 * notably, version tracking: every update compares the new state against
 * the stored state, records which fields changed, and bumps the entity's
 * version number accordingly (a "major" change like renaming bumps more
 * than a "minor" one like editing a description).
 */
public abstract class EntityRepository<T extends Entity> {

  protected final EntityDao<T> dao;
  protected final String entityType;

  /** Fields whose change is considered significant enough to bump the major version. */
  protected final List<String> majorVersionFields;

  protected EntityRepository(EntityDao<T> dao, String entityType, List<String> majorVersionFields) {
    this.dao = dao;
    this.entityType = entityType;
    this.majorVersionFields = majorVersionFields;
  }

  public T create(T entity) {
    entity.setId(UUID.randomUUID().toString());
    entity.setVersion(1.0);
    prepare(entity);
    dao.insert(entity);
    return entity;
  }

  public T getById(String id) {
    T entity = dao.findById(id);
    if (entity == null) {
      throw new EntityNotFoundException(entityType, id);
    }
    return entity;
  }

  /**
   * Updates an entity, computing a version bump from what actually changed.
   * This is the core pattern: diff old vs. new, classify the change, store
   * both the updated entity and a record of what changed.
   */
  public T update(String id, T updatedEntity) {
    T original = getById(id);
    prepare(updatedEntity);

    ChangeDescription change = computeChange(original, updatedEntity);
    if (change.isEmpty()) {
      return original; // no-op update, nothing to persist
    }

    double newVersion =
        change.hasMajorChange() ? nextMajorVersion(original.getVersion()) : nextMinorVersion(original.getVersion());
    updatedEntity.setId(id);
    updatedEntity.setVersion(newVersion);

    dao.update(updatedEntity);
    dao.recordChange(id, newVersion, change);
    return updatedEntity;
  }

  public void delete(String id) {
    getById(id); // ensures it exists, matching original behavior of failing fast
    dao.softDelete(id);
  }

  public List<T> list(int limit, String after) {
    return dao.list(limit, after);
  }

  /** Hook for subclasses to fill in derived fields (e.g. fully-qualified name) before saving. */
  protected void prepare(T entity) {}

  private ChangeDescription computeChange(T original, T updated) {
    ChangeDescription change = new ChangeDescription();
    for (String field : entityFields()) {
      Object oldValue = original.getFieldValue(field);
      Object newValue = updated.getFieldValue(field);
      if (!Objects.equals(oldValue, newValue)) {
        boolean major = majorVersionFields.contains(field);
        change.addFieldChange(field, oldValue, newValue, major);
      }
    }
    return change;
  }

  /** Subclasses declare which fields participate in change tracking. */
  protected abstract List<String> entityFields();

  private static double nextMajorVersion(double current) {
    return Math.floor(current) + 1.0;
  }

  private static double nextMinorVersion(double current) {
    return current + 0.1;
  }

  // --- Minimal supporting types just so this example compiles standalone ---

  public interface EntityDao<T> {
    void insert(T entity);

    void update(T entity);

    T findById(String id);

    void softDelete(String id);

    List<T> list(int limit, String after);

    void recordChange(String id, double version, ChangeDescription change);
  }

  public interface Entity {
    String getId();

    void setId(String id);

    double getVersion();

    void setVersion(double version);

    Object getFieldValue(String fieldName);
  }

  public static class ChangeDescription {
    private final List<FieldChange> fieldChanges = new java.util.ArrayList<>();

    void addFieldChange(String field, Object oldValue, Object newValue, boolean major) {
      fieldChanges.add(new FieldChange(field, oldValue, newValue, major));
    }

    public boolean isEmpty() {
      return fieldChanges.isEmpty();
    }

    public boolean hasMajorChange() {
      return fieldChanges.stream().anyMatch(f -> f.major);
    }

    public List<FieldChange> getFieldChanges() {
      return fieldChanges;
    }

    public record FieldChange(String field, Object oldValue, Object newValue, boolean major) {}
  }

  public static class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityType, String id) {
      super(entityType + " not found: " + id);
    }
  }
}
