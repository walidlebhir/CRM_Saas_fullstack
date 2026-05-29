package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OpportunityResponseDTO(
    Long id,
    String title,
    BigDecimal amount,
    OpportunityStage stage,
    Integer probabilirty,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    CustomerInfo customer
) {
    public record CustomerInfo(UUID id, String name, String email) {}
}
