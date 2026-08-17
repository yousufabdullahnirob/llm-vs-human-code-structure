package com.example.metadata.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.diff.JsonDiff;
import java.io.IOException;

/**
 * Central place for JSON (de)serialization so the rest of the codebase
 * doesn't create its own ObjectMapper instances with inconsistent settings.
 *
 * Keeps a small number of pre-configured mappers for different situations
 * (strict vs. lenient parsing, whether null fields are included) and some
 * generic helpers built on top of them, including a JSON Patch diff helper
 * used to compute PATCH request bodies from before/after entity states.
 */
public final class JsonUtils {

  private JsonUtils() {}

  // Default mapper: strict parsing, omits null fields on output.
  private static final ObjectMapper MAPPER = buildMapper(true, false);

  // Lenient mapper: tolerates unknown fields, useful when reading data
  // written by a newer version of the schema than this code knows about.
  private static final ObjectMapper LENIENT_MAPPER = buildMapper(false, false);

  // Include-nulls mapper: needed when computing a full snapshot for diffing,
  // since omitted null fields would be invisible to a JSON Patch diff.
  private static final ObjectMapper INCLUDE_NULLS_MAPPER = buildMapper(true, true);

  private static ObjectMapper buildMapper(boolean failOnUnknownProperties, boolean includeNulls) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
    if (!includeNulls) {
      mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
    }
    return mapper;
  }

  public static String toJson(Object object) {
    try {
      return MAPPER.writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException("Failed to serialize object to JSON", e);
    }
  }

  public static <T> T readValue(String json, Class<T> clazz) {
    return readValue(json, clazz, false);
  }

  public static <T> T readValue(String json, Class<T> clazz, boolean lenient) {
    try {
      ObjectMapper mapper = lenient ? LENIENT_MAPPER : MAPPER;
      return mapper.readValue(json, clazz);
    } catch (IOException e) {
      throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
    }
  }

  public static JsonNode readTree(String json) {
    try {
      return MAPPER.readTree(json);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse JSON", e);
    }
  }

  /**
   * Computes an RFC 6902 JSON Patch describing how to turn `before` into
   * `after`. Both objects are serialized with the include-nulls mapper so
   * that fields cleared out in `after` are represented as explicit null
   * removals rather than silently omitted.
   */
  public static JsonNode getJsonPatch(Object before, Object after) {
    JsonNode beforeNode = INCLUDE_NULLS_MAPPER.valueToTree(before);
    JsonNode afterNode = INCLUDE_NULLS_MAPPER.valueToTree(after);
    return JsonDiff.asJson(beforeNode, afterNode);
  }

  public static <T> T applyPatch(T target, JsonNode patch, Class<T> clazz) {
    try {
      JsonNode targetNode = INCLUDE_NULLS_MAPPER.valueToTree(target);
      JsonNode patchedNode = com.github.fge.jsonpatch.JsonPatch.fromJson(patch).apply(targetNode);
      return INCLUDE_NULLS_MAPPER.treeToValue(patchedNode, clazz);
    } catch (JsonPatchException | IOException e) {
      throw new RuntimeException("Failed to apply JSON Patch", e);
    }
  }

  public static <T> T deepCopy(T object, Class<T> clazz) {
    return readValue(toJson(object), clazz);
  }
}
