package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.LeadStatus;

public record StatusRequest(
        LeadStatus status
) {
}
