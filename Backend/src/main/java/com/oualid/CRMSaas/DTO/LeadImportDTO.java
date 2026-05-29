package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.LeadSource;

public record LeadImportDTO(
    String firstname,
    String lastname,
    String email,
    String phone,
    LeadSource source
) {}
