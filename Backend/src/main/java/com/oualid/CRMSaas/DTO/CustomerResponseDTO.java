package com.oualid.CRMSaas.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponseDTO(
    UUID id,
    String name,
    String email,
    String phone,
    String website,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
