package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.LeadSource;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;

public record LeadDTO(
        String firstname ,
        String lastname ,
        String email ,
        String phone,
        LeadSource source,
        LeadStatus status

) {
}
