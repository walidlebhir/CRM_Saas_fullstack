package com.oualid.CRMSaas.Services.WebHooksService;


import com.oualid.CRMSaas.DTO.LeadDTO;
import com.oualid.CRMSaas.Entity.Company;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;
import com.oualid.CRMSaas.Entity.Enum.RoleUser;
import com.oualid.CRMSaas.Entity.Lead;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Repository.CompanyRepository;
import com.oualid.CRMSaas.Repository.LeadRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import com.oualid.CRMSaas.Tools.AdapterSystem.LeadAdapter;
import com.oualid.CRMSaas.Tools.AdapterSystem.LeadAdapterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebHooksLeadService {

    private UserRepository userrepo ;
    private CompanyRepository companyRepo ;
    private LeadRepository leadrepo ;

    public WebHooksLeadService(UserRepository userrepo, LeadRepository leadrepo , CompanyRepository companyrepo ){
        this.companyRepo =companyrepo ;
        this.userrepo = userrepo ;
        this.leadrepo = leadrepo ;
    }
    /**
     * Methode pour procssuss lead
     * methode pour sauvgareder lead dans database
     */

    public ResponseEntity<String> ProccesLead(String source , Object pyload , String token ){
        LeadAdapter leadtype = LeadAdapterFactory.getAdapter(source);
        LeadDTO objectlead = leadtype.convert(pyload);
        // Rapele au fonction save Lead

        Company company = this.companyRepo.findByTokenWebHooks(token);
        // user
        List<User> usercompany = this.userrepo.findByCompany(company);
        User commerciale = null;
        for(User user : usercompany) {
            if (user.getRole().equals(RoleUser.COMMERCIAL)) {
                commerciale = user;
                break;
            }
        }
        Lead newLead = Lead.builder()
                .firstname(objectlead.firstname())
                .lastname(objectlead.lastname())
                .email(objectlead.email())
                .phone(objectlead.phone())
                .status(LeadStatus.NEW)
                .company(company)
                .assignedTo(commerciale)
                .build();
        Lead leadSaved = this.leadrepo.save(newLead);

        return ResponseEntity.status(HttpStatus.CREATED).body("lead save aves succes ");

    }

    public void CreateNewLead(){

    }

}
