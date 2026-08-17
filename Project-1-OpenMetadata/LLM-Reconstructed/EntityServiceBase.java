package com.example.metadata.sdk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generic base class for a typed HTTP client "service" (e.g. UserService,
 * TeamService) that talks to a REST API for a single entity type.
 *
 * The interesting piece of the pattern: when you fetch an entity, we keep a
 * snapshot of its JSON. When you later call update(), we diff the new state
 * against that snapshot (instead of against an empty object) so that fields
 * the caller cleared out (set to null) are correctly included in the patch,
 * even though null fields are normally omitted from serialization.
 */
public abstract class EntityServiceBase<T> {

  private static final int MAX_SNAPSHOTS = 500;

  protected final HttpClient httpClient;
  protected final String basePath;
  protected final ObjectMapper objectMapper;

  // Bounded LRU cache: entity id -> last-seen JSON snapshot.
  private final Map<String, JsonNode> snapshots =
      Collections.synchronizedMap(
          new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, JsonNode> eldest) {
              return size() > MAX_SNAPSHOTS;
            }
          });

  protected EntityServiceBase(HttpClient httpClient, String basePath) {
    this.httpClient = httpClient;
    this.basePath = basePath;
    this.objectMapper = new ObjectMapper();
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  /** Subclasses provide the concrete entity class for JSON (de)serialization. */
  protected abstract Class<T> getEntityClass();

  public T create(T entity) {
    return httpClient.execute(HttpMethod.POST, basePath, entity, getEntityClass());
  }

  public T get(String id) {
    T entity = httpClient.execute(HttpMethod.GET, basePath + "/" + id, null, getEntityClass());
    storeSnapshot(id, entity);
    return entity;
  }

  public T update(String id, T updatedEntity) {
    JsonNode baseline = snapshots.get(id);
    JsonNode target = objectMapper.valueToTree(updatedEntity);

    JsonNode patch =
        (baseline != null) ? JsonDiff.diff(baseline, target) : JsonDiff.diff(objectMapper.createObjectNode(), target);

    T result = httpClient.execute(HttpMethod.PATCH, basePath + "/" + id, patch, getEntityClass());
    storeSnapshot(id, result);
    return result;
  }

  public void delete(String id) {
    httpClient.execute(HttpMethod.DELETE, basePath + "/" + id, null, Void.class);
    snapshots.remove(id);
  }

  private void storeSnapshot(String id, T entity) {
    snapshots.put(id, objectMapper.valueToTree(entity));
  }

  // --- Minimal supporting types just so this example compiles standalone ---

  public interface HttpClient {
    <R> R execute(HttpMethod method, String path, Object body, Class<R> responseType);
  }

  public enum HttpMethod {
    GET,
    POST,
    PATCH,
    DELETE
  }

  /** Stand-in for a real JSON Patch diff library (e.g. zjsonpatch). */
  private static final class JsonDiff {
    static JsonNode diff(JsonNode before, JsonNode after) {
      // Placeholder: a real implementation computes an RFC 6902 JSON Patch
      // between `before` and `after`. Omitted here since it's a library
      // call in the original, not part of the architectural pattern.
      throw new UnsupportedOperationException("plug in a JSON Patch library, e.g. zjsonpatch");
    }
  }
}
