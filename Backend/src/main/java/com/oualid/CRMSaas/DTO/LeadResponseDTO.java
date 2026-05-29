package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.LeadSource;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record LeadResponseDTO(
    UUID id,
    String firstname,
    String lastname,
    String email,
    String phone,
    LeadSource source,
    LeadStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
