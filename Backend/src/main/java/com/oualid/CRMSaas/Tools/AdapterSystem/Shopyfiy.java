package com.oualid.CRMSaas.Tools.AdapterSystem;

import com.oualid.CRMSaas.DTO.LeadDTO;

import java.util.Map;

public class Shopyfiy implements LeadAdapter {
    @Override
    public LeadDTO convert(Object payload) {

        Map<String, Object> data = (Map<String, Object>) payload;

        LeadDTO lead = new LeadDTO(
                (String) data.get("firstname") ,
                (String) data.get("lastname"),
                (String) data.get("email"),
                (String)  data.get("phone")
        );

        return lead;
    }
}
