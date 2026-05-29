package com.oualid.CRMSaas.DTO;

import java.util.List;

public record ImportLeadDistributeDTO(
    List<LeadImportDTO> leads,
    boolean distributeRandomly
) {}
