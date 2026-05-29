package com.oualid.CRMSaas.Tools;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public interface CrmEnum {

    String name();

    default String getDisplayName() {
        return name();
    }

    default Map<String, ? extends Enum<?>> getAliases() {
        return Map.of();
    }

    static <T extends Enum<T>> T fromString(Class<T> enumType, String value, Map<String, T> extraAliases) {
        if (value == null || value.isBlank()) return null;

        String normalized = value.trim().toLowerCase(Locale.ROOT);

        if (extraAliases.containsKey(normalized)) {
            return extraAliases.get(normalized);
        }

        for (T constant : enumType.getEnumConstants()) {
            if (constant.name().toLowerCase(Locale.ROOT).equals(normalized)) {
                return constant;
            }
            if (constant instanceof CrmEnum crm) {
                String display = crm.getDisplayName().toLowerCase(Locale.ROOT);
                if (display.equals(normalized)) return constant;
            }
        }

        throw new IllegalArgumentException(
            "Valeur invalide pour " + enumType.getSimpleName() + " : '" + value + "'. " +
            "Valeurs acceptées : " + java.util.Arrays.stream(enumType.getEnumConstants())
                .map(Enum::name)
                .collect(java.util.stream.Collectors.joining(", "))
        );
    }

    static <T extends Enum<T>> T fromString(Class<T> enumType, String value) {
        return fromString(enumType, value, new HashMap<>());
    }
}