package com.oualid.CRMSaas.DTO;

import jakarta.validation.constraints.NotBlank;

public record TaskReassignRequest(
        @NotBlank(message = "L'email de l'utilisateur est obligatoire")
        String email
) {
}
