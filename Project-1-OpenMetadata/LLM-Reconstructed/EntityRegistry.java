package com.example.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Central static registry that maps entity type names (e.g. "user", "team")
 * to the repository responsible for persisting and loading that entity.
 *
 * This mirrors a common pattern in larger metadata-catalog systems: rather
 * than passing repository instances around everywhere, code looks them up
 * by entity type through this single access point.
 */
public final class EntityRegistry {

  private EntityRegistry() {}

  // Well-known entity type names. Real systems often have dozens of these;
  // this example keeps just a few to show the shape of the pattern.
  public static final String USER = "user";
  public static final String TEAM = "team";
  public static final String TABLE = "table";

  // Tracks every entity type that has been registered, in sorted order.
  private static final Set<String> KNOWN_ENTITY_TYPES = new TreeSet<>();

  // entity type -> repository instance
  private static final Map<String, EntityRepository<?>> REPOSITORIES = new HashMap<>();

  // entity type -> concrete Java class backing that entity
  private static final Map<String, Class<?>> ENTITY_CLASSES = new HashMap<>();

  /**
   * Registers a repository for a given entity type. Called once at startup
   * for each entity type the system supports.
   */
  public static void register(String entityType, EntityRepository<?> repository, Class<?> entityClass) {
    REPOSITORIES.put(entityType, repository);
    ENTITY_CLASSES.put(entityType, entityClass);
    KNOWN_ENTITY_TYPES.add(entityType);
  }

  @SuppressWarnings("unchecked")
  public static <T> EntityRepository<T> getRepository(String entityType) {
    EntityRepository<?> repository = REPOSITORIES.get(entityType);
    if (repository == null) {
      throw new IllegalArgumentException("No repository registered for entity type: " + entityType);
    }
    return (EntityRepository<T>) repository;
  }

  public static Class<?> getEntityClass(String entityType) {
    Class<?> clazz = ENTITY_CLASSES.get(entityType);
    if (clazz == null) {
      throw new IllegalArgumentException("Unknown entity type: " + entityType);
    }
    return clazz;
  }

  public static boolean isKnownEntityType(String entityType) {
    return KNOWN_ENTITY_TYPES.contains(entityType);
  }

  public static Set<String> getKnownEntityTypes() {
    return KNOWN_ENTITY_TYPES;
  }

  /**
   * Convenience bootstrap method showing how registration typically happens
   * once at application startup, wiring each entity type to its repository.
   */
  public static void bootstrap(
      Supplier<EntityRepository<User>> userRepoSupplier,
      Supplier<EntityRepository<Team>> teamRepoSupplier,
      Supplier<EntityRepository<Table>> tableRepoSupplier) {
    register(USER, userRepoSupplier.get(), User.class);
    register(TEAM, teamRepoSupplier.get(), Team.class);
    register(TABLE, tableRepoSupplier.get(), Table.class);
  }

  // --- Minimal supporting types just so this example compiles standalone ---

  public interface EntityRepository<T> {
    T getById(String id);
    void save(T entity);
  }

  public static class User {}

  public static class Team {}

  public static class Table {}
}
