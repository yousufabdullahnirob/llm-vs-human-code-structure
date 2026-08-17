package com.example.metadata.llmgen;

import java.util.HashMap;
import java.util.Map;

/**
 * LLM-generated helper for building a "user" fields map to pass into
 * EntityRepository.create(...). Not an entity class or interface — just
 * a static factory, since no Entity contract/interface was generated.
 */
public class EntityUser {
    public static Map<String, Object> create(String name, String email) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", name);
        fields.put("email", email);
        return fields;
    }
}
