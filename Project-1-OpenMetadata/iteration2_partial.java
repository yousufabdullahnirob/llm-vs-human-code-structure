// PROMPT v2 (given to the LLM):
// "Separate the storage logic into its own class, distinct from the
//  entity-type registry. Track major vs minor version changes based on
//  which fields changed (e.g. changing 'name' or 'email' should be a
//  major change, anything else a minor change)."
//
// OBSERVED OUTPUT — two classes now exist (EntityStore, EntityTypeRegistry),
// which is an improvement over iteration 1, but EntityStore still contains
// the versioning logic inline (hardcoded field-name checks), and
// EntityTypeRegistry calls directly into EntityStore's internals rather
// than going through any interface — so the two classes are still tightly
// coupled and there is still no DAO-style abstraction.

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class EntityStore {
    static final Map<String, Map<String, Object>> data = new HashMap<>();
    static final Map<String, Double> versions = new HashMap<>();

    static String insert(Map<String, Object> fields) {
        String id = UUID.randomUUID().toString();
        data.put(id, fields);
        versions.put(id, 1.0);
        return id;
    }

    static void update(String id, Map<String, Object> newFields) {
        Map<String, Object> existing = data.get(id);
        double version = versions.get(id);
        boolean major = false;
        for (String key : newFields.keySet()) {
            if (!java.util.Objects.equals(existing.get(key), newFields.get(key))) {
                if (key.equals("name") || key.equals("email")) {
                    major = true;
                }
            }
        }
        version = major ? Math.floor(version) + 1.0 : version + 0.1;
        existing.putAll(newFields);
        versions.put(id, version);
    }
}

class EntityTypeRegistry {
    static final java.util.List<String> knownTypes = new java.util.ArrayList<>();

    static String createEntity(String type, Map<String, Object> fields) {
        if (!knownTypes.contains(type)) {
            knownTypes.add(type);
        }
        // directly reaches into EntityStore rather than going through
        // an interface/abstraction
        return EntityStore.insert(fields);
    }
}

// STRUCTURAL FLAW OBSERVED:
// - Two classes exist now, which is progress, but there is still no
//   interface/abstract class anywhere (no DAO contract, no Entity
//   contract) — so the "abstraction" dimension of the H system is
//   still entirely absent.
// - EntityTypeRegistry reaches directly into EntityStore's static
//   fields/methods: registry and storage are still effectively one
//   coupled unit, just split across two files.
// - No custom exception type, no per-field change record (no
//   equivalent of ChangeDescription/FieldChange) — version bump logic
//   only tracks a boolean "was it major", losing the detail of which
//   fields actually changed.
// REASON FOR NEXT ITERATION:
// Give the LLM an explicit, component-by-component specification
// (mirroring the actual H structure) rather than a general constraint,
// to see whether it can reproduce the DAO abstraction and the
// change-record concept when told exactly what components to build.
