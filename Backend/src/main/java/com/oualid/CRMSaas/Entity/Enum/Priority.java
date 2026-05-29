package com.oualid.CRMSaas.Entity.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum Priority {
    LOW("Faible"),
    MEDIUM("Moyenne"),
    HIGH("Urgente");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    private static final Map<String, Priority> MAPPINGS = new HashMap<>();

    static {
        for (Priority p : values()) {
            MAPPINGS.put(p.name().toLowerCase(), p);
            MAPPINGS.put(p.displayName.toLowerCase(), p);
        }
        MAPPINGS.put("basse", LOW);
        MAPPINGS.put("haute", HIGH);
        MAPPINGS.put("elevee", HIGH);
        MAPPINGS.put("élevée", HIGH);
        MAPPINGS.put("normale", MEDIUM);
    }

    @JsonCreator
    public static Priority fromString(String value) {
        if (value == null || value.isBlank()) return null;
        Priority result = MAPPINGS.get(value.trim().toLowerCase());
        if (result == null) {
            throw new IllegalArgumentException(
                "Valeur invalide pour Priority : '" + value + "'. Valeurs acceptées : " +
                "HIGH/Urgente, MEDIUM/Moyenne, LOW/Faible"
            );
        }
        return result;
    }
}
