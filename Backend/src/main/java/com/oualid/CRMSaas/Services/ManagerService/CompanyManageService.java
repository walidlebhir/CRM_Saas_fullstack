package com.oualid.CRMSaas.Services.ManagerService;


import com.oualid.CRMSaas.DTO.CompanyRequestDTO;
import com.oualid.CRMSaas.Entity.AuditLogger;
import com.oualid.CRMSaas.Entity.Company;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.CompanyNotFound;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.CompanyRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import com.oualid.CRMSaas.Tools.LoggerObserver.EventManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CompanyManageService {
    // injcetion de dependance :

    private CompanyRepository companyrepository ;
    private UserRepository userrepo ;
    private EventManager eventmangerAudit ;


    public  CompanyManageService(CompanyRepository companyrepository ,
                                 UserRepository userrepo,
                                 EventManager eventaudit ){
        this.companyrepository = companyrepository ;
        this.userrepo = userrepo ;
        this.eventmangerAudit = eventaudit ;
    }



    @Transactional
    public Company CreateNewCompany(CompanyRequestDTO datacompanycreate ,String username_manager ){
        try{
            // creation company
            Company NewCompany = Company.builder()
                    .name(datacompanycreate.name())
                    .industrie(datacompanycreate.industrie())
                    .phone(datacompanycreate.phone())
                    .email(datacompanycreate.email())
                    .webSite(datacompanycreate.website())
                    .address(datacompanycreate.address())
                    .build();
            this.companyrepository.save(NewCompany) ;
            // asigne company au user create :
            User user_manager = this.userrepo.findByEmail(username_manager);
            if(user_manager == null){
                throw new UserNotFoundException("manager not found ") ;
            }

            user_manager.setCompany(NewCompany);
            this.userrepo.save(user_manager) ;
            AuditLogger logger = AuditLogger.builder()
                    .type("CREATED COMPANY ")
                    .log("MANAGER CREATE NEW COMPANY")
                    .build();
            this.eventmangerAudit.publish(logger);
            return  NewCompany ;
        }catch(Exception ex ){
            // debug
            System.out.println("Problemme dans le service "+ex.getMessage()) ;
            throw  new RuntimeException("problemme dans le service ");

        }

    }

    // modefication donnes de company :
    public Company updateDataCompany(CompanyRequestDTO datarequestupdate , UUID id_company){
        try{
            Company company_updated = this.companyrepository.findById(id_company).orElse(null) ;
            if(company_updated == null){
                throw  new CompanyNotFound("company not found ") ;
            }

            // mapping data
            company_updated.setName(datarequestupdate.name());
            company_updated.setEmail(datarequestupdate.email());
            company_updated.setPhone(datarequestupdate.phone());
            company_updated.setAddress(datarequestupdate.address());
            company_updated.setWebSite(datarequestupdate.website());
            company_updated.setIndustrie(datarequestupdate.website());

            Company company_saved = this.companyrepository.save(company_updated) ;
            return  company_saved ;
        }catch(Exception ex ){
            // debug
            System.out.println("Problemme dans le service "+ex.getMessage()) ;
            throw  new RuntimeException("problemme dans le service ");

        }
    }

    // recuperation des info company


    public Company ShowCompany( String username_manager ){
        try{
            User manageruser = this.userrepo.findByEmail(username_manager) ;
            Company company_manager = manageruser.getCompany() ;
            return  company_manager ;
        }catch(Exception e ){
            System.out.println("[LOG] :  "+e.getMessage()) ;
            throw  new RuntimeException("problemme dans le service ");
        }

    }

    public Boolean ChekEntrepirseCreate(String usernamemanager ){
        try{
            User manager = this.userrepo.findByEmail(usernamemanager);
            if(manager == null){
                throw  new UserNotFoundException("manager dont existe") ;
            }
            Company company_manager = manager.getCompany();
            if(company_manager == null){
                return  false ;
            }

            return  true;
        }catch(Exception e){
            System.out.println("[LOG] :  "+e.getMessage()) ;
            throw  new RuntimeException("problemme dans le service ");
        }

    }
}
