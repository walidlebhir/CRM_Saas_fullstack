package com.oualid.CRMSaas.Tools.AdapterSystem;

import com.oualid.CRMSaas.DTO.LeadDTO;

public interface LeadAdapter {
    LeadDTO convert(Object externalPayload);
}
