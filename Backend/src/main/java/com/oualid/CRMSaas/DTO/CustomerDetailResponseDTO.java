package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CustomerDetailResponseDTO(
    UUID id,
    String name,
    String email,
    String phone,
    String website,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<OpportunitySummary> opportunities
) {
    public record OpportunitySummary(
        Long id,
        String title,
        BigDecimal amount,
        OpportunityStage stage,
        Integer probabilirty,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}
}
