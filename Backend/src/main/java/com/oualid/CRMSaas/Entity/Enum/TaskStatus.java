package com.oualid.CRMSaas.Entity.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    private static final Map<String, TaskStatus> MAPPINGS = new HashMap<>();

    static {
        for (TaskStatus s : values()) {
            MAPPINGS.put(s.name().toLowerCase(), s);
        }
        MAPPINGS.put("à faire", TODO);
        MAPPINGS.put("a faire", TODO);
        MAPPINGS.put("en cours", IN_PROGRESS);
        MAPPINGS.put("terminé", DONE);
        MAPPINGS.put("termine", DONE);
        MAPPINGS.put("fait", DONE);
    }

    @JsonCreator
    public static TaskStatus fromString(String value) {
        if (value == null || value.isBlank()) return null;
        TaskStatus result = MAPPINGS.get(value.trim().toLowerCase());
        if (result == null) {
            throw new IllegalArgumentException(
                "Valeur invalide pour TaskStatus : '" + value + "'. Valeurs acceptées : " +
                "TODO, IN_PROGRESS, DONE"
            );
        }
        return result;
    }
}
