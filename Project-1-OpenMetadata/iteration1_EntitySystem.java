// PROMPT v1 (given to the LLM):
// "Write Java code for a system that stores entities like users and teams
//  with version tracking."
//
// OBSERVED OUTPUT — everything in a single file/class, no layering,
// no registry concept, versioning is a flat increment with no notion of
// major/minor change classification.

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntitySystem {

    private static final Map<String, Map<String, Object>> entities = new HashMap<>();
    private static final Map<String, Integer> versions = new HashMap<>();

    public static String addEntity(String type, Map<String, Object> data) {
        String id = UUID.randomUUID().toString();
        entities.put(id, data);
        versions.put(id, 1);
        return id;
    }

    public static void updateEntity(String id, Map<String, Object> newData) {
        entities.put(id, newData);
        versions.put(id, versions.get(id) + 1);
    }

    public static Map<String, Object> getEntity(String id) {
        return entities.get(id);
    }

    public static void deleteEntity(String id) {
        entities.remove(id);
        versions.remove(id);
    }
}

// STRUCTURAL FLAW OBSERVED:
// - No separation of concerns at all: storage, versioning, and entity
//   "type" handling are conflated into one static class.
// - No distinction between entity types (user vs team) beyond a string.
// - Version bump is always +1 regardless of what changed — no
//   major/minor classification, which was central to the H system.
// - No registry, no DAO abstraction, no interfaces of any kind.
// REASON FOR NEXT ITERATION:
// Add explicit constraints requiring separation between storage and
// entity-type registration, and require version bumps to depend on
// which fields changed.
