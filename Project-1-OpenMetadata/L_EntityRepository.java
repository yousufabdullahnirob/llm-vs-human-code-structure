// PROMPT v3 (given to the LLM):
// "Build a Java entity persistence system with these exact components:
//  1. An EntityRepository responsible for create/getById/update/delete.
//  2. A separate DAO interface that EntityRepository depends on for
//     actual storage (do not let EntityRepository touch storage
//     directly).
//  3. A separate EntityRegistry responsible only for looking up which
//     repository handles which entity type — it should not perform any
//     storage itself.
//  4. On update, compute which fields changed and record that as a
//     structured object (not just a boolean), and bump version: whole
//     number for a 'major' field change (name/email), +0.1 otherwise."
//
// OBSERVED OUTPUT — despite the explicit component list, the LLM still
// collapsed the DAO interface and the change-record object into the
// same class as the repository logic, and merged registry lookup into
// the same static class as well. This is the FINAL output used as the
// "L" system for metric comparison against H (EntityRepository.java +
// EntityRegistry.java).

package com.example.metadata.llmgen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;

/**
 * LLM-generated entity repository + registry, collapsed into one class.
 * Handles storage, versioning, and entity-type lookup for any entity type.
 */
public class EntityRepository {

    // entity type -> (id -> stored fields map)   [storage — should have
    // been behind a DAO interface, per the prompt]
    private static final Map<String, Map<String, Map<String, Object>>> STORE = new HashMap<>();

    // entity type -> (id -> version)
    private static final Map<String, Map<String, Double>> VERSIONS = new HashMap<>();

    // known entity types   [registry responsibility, mixed into this
    // same class despite the prompt asking for it to be separate]
    private static final List<String> KNOWN_TYPES = new ArrayList<>();

    public static void registerType(String entityType) {
        if (!KNOWN_TYPES.contains(entityType)) {
            KNOWN_TYPES.add(entityType);
            STORE.put(entityType, new HashMap<>());
            VERSIONS.put(entityType, new HashMap<>());
        }
    }

    public static String create(String entityType, Map<String, Object> fields) {
        registerType(entityType);
        String id = UUID.randomUUID().toString();
        fields.put("id", id);
        STORE.get(entityType).put(id, fields);
        VERSIONS.get(entityType).put(id, 1.0);
        return id;
    }

    public static Map<String, Object> getById(String entityType, String id) {
        Map<String, Object> entity = STORE.get(entityType).get(id);
        if (entity == null) {
            // no custom exception type — generic RuntimeException used
            throw new RuntimeException(entityType + " not found: " + id);
        }
        return entity;
    }

    public static void update(String entityType, String id, Map<String, Object> newFields) {
        Map<String, Object> existing = getById(entityType, id);
        double version = VERSIONS.get(entityType).get(id);

        boolean changed = false;
        for (String key : newFields.keySet()) {
            Object oldVal = existing.get(key);
            Object newVal = newFields.get(key);
            if (oldVal == null ? newVal != null : !oldVal.equals(newVal)) {
                changed = true;
                // hardcoded field-name check instead of a configurable
                // "majorVersionFields" list, and no structured
                // ChangeDescription/FieldChange record is built at all
                if (key.equals("name") || key.equals("email")) {
                    version = Math.floor(version) + 1.0;
                } else {
                    version = version + 0.1;
                }
            }
        }

        if (changed) {
            existing.putAll(newFields);
            VERSIONS.get(entityType).put(id, version);
        }
    }

    public static void delete(String entityType, String id) {
        getById(entityType, id);
        STORE.get(entityType).remove(id);
        VERSIONS.get(entityType).remove(id);
    }

    public static List<String> listTypes() {
        return KNOWN_TYPES;
    }
}
