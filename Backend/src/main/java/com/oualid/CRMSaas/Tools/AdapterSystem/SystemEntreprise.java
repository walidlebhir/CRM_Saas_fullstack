package com.oualid.CRMSaas.Tools.AdapterSystem;

import com.oualid.CRMSaas.DTO.LeadDTO;
import com.oualid.CRMSaas.Entity.Enum.LeadSource;

import java.util.Map;

public class SystemEntreprise implements LeadAdapter {
    @Override
    public LeadDTO convert(Object externalPayload) {
        Map<String, Object> data = (Map<String, Object>) externalPayload;

        LeadDTO lead = new LeadDTO(
                (String) data.get("prenom") ,
                (String) data.get("nom") ,
                (String)  data.get("email") ,
                (String) data.get("phonenumber"),
                (LeadSource)  data.get("source"),
                null
        );


        return lead;
    }
}
