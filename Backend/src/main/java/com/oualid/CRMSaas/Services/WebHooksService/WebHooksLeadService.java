package com.oualid.CRMSaas.Services.WebHooksService;


import com.oualid.CRMSaas.DTO.LeadDTO;
import com.oualid.CRMSaas.Tools.AdapterSystem.LeadAdapter;
import com.oualid.CRMSaas.Tools.AdapterSystem.LeadAdapterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WebHooksLeadService {


    /**
     * Methode pour procssuss lead
     * methode pour sauvgareder lead dans database
     */

    public ResponseEntity<String> ProccesLead(String source , Object pyload){
        LeadAdapter leadtype = LeadAdapterFactory.getAdapter(source);
        LeadDTO objectlead = leadtype.convert(pyload);
        // Rapele au fonction save Lead
        return ResponseEntity.status(HttpStatus.CREATED).body("lead save aves succes ");

    }

    public void CreateNewLead(){

    }

}
