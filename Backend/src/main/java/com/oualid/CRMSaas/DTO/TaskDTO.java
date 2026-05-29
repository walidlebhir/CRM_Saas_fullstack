package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.Priority;
import com.oualid.CRMSaas.Entity.Enum.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskDTO(
        @NotBlank(message = "Le titre est obligatoire")
        String title,

        String description,

        LocalDate dueDate,

        @NotNull(message = "Le statut est obligatoire")
        TaskStatus status,

        @NotNull(message = "La priorité est obligatoire")
        Priority priority,

        Long id_opportunity
) {
}
