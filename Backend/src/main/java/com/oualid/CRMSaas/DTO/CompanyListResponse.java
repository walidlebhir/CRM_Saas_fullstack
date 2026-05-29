package com.oualid.CRMSaas.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyListResponse(
    UUID id,
    String name,
    String industrie,
    String email,
    String phone,
    String address,
    String webSite,
    boolean hasWebhookToken,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    long totalUsers,
    long totalLeads
) {}
