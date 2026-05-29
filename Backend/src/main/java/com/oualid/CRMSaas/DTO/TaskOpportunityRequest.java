package com.oualid.CRMSaas.DTO;

import jakarta.validation.constraints.NotNull;

public record TaskOpportunityRequest(
        @NotNull(message = "L'ID de l'opportunité est obligatoire")
        Long id_opportunity
) {
}
