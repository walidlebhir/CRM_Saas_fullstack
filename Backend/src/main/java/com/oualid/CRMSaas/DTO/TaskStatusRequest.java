package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusRequest(
        @NotNull(message = "Le statut est obligatoire")
        TaskStatus status
) {
}
