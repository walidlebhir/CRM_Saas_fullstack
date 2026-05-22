package com.oualid.CRMSaas.Services.ComercialeService;


import com.oualid.CRMSaas.DTO.AffectOpportunityRequest;
import com.oualid.CRMSaas.DTO.OpportuniteRequest;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;
import com.oualid.CRMSaas.Entity.Opportunity;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.ResourceNotFoundException;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.CustomerRepository;
import com.oualid.CRMSaas.Repository.OpportunityRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import jakarta.persistence.Transient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OpportunityService {

    private  final OpportunityRepository opportuniterepository ;
    private  final UserRepository userrepository  ;
    private final CustomerRepository customerrepository ;
    public OpportunityService(OpportunityRepository opportuniterepo,
                              UserRepository usrrepo ,
                              CustomerRepository customerrepository ){
        this.opportuniterepository = opportuniterepo ;
        this.userrepository = usrrepo;
        this.customerrepository = customerrepository;
    }


    // create oportunite
    public Opportunity CreateNewOpportunite(OpportuniteRequest dataopertunity  , String comerciale_username){

        try{
            User comercialeData = this.userrepository.findByEmail(comerciale_username);
            if(comercialeData == null){
                throw new UserNotFoundException("comercialle not found ") ;
            }

            // create new Oportunity :
            Opportunity NewOpportunity = Opportunity.builder()
                    .title(dataopertunity.title())
                    .stage(dataopertunity.stage())
                    .amount(dataopertunity.amount())
                    .description(dataopertunity.description())
                    .probabilirty(dataopertunity.probability())
                    .assignedTo(comercialeData)
                    .build();
            Opportunity opportunityCreated = this.opportuniterepository.save(NewOpportunity);
            return  opportunityCreated ;
        }catch(Exception e){
            // debug
            System.out.println("Problemme dans le service "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }

    }

    public List<OpportunityStage> getAllStageOpportunite(){
        return  List.of(OpportunityStage.values()) ;
    }

    public List<Opportunity> ListAllOpportunity(String user_name){
        try{
            User comerciale = this.userrepository.findByEmail(user_name) ;
            if(comerciale == null){
                throw  new UserNotFoundException("commerciale not found") ;
            }

            List<Opportunity> dataopportunity = this.opportuniterepository.findByAssignedTo(comerciale);
            return  dataopportunity ;

        }catch(Exception e){
            // debug
            System.out.println("Problemme dans le service "+e.getMessage());
            throw new RuntimeException("problemme dans le service ") ;
        }
    }

    public Opportunity ShowOpportunity(Long opportunity_id){
        try{
            Opportunity opportunity = this.opportuniterepository.findById(opportunity_id).orElseThrow(()->new ResourceNotFoundException("this opportunity dont existe "));
            return  opportunity ;
        }catch(Exception e){
            // debug
            System.out.println("Porblemme dans le service "+e.getMessage()) ;
            throw  new RuntimeException("problemme dans le service ");

        }
    }

    public ResponseEntity<String> deleteOpportunity(Long id_opportunity ){
        try{
           this.opportuniterepository.deleteById(id_opportunity);
           return  ResponseEntity.status(HttpStatus.CREATED).body("opportunity is deleted with succes") ;

        }catch(Exception e){
            System.out.println("Probelemme dans le service "+e.getMessage());
            throw new RuntimeException("problemme dans le service ");
        }
    }

    /**
     * --> Afceter une opportunite a client :
     */

    @Transactional
    public  ResponseEntity<String> AffceteOpportunityToclinet(AffectOpportunityRequest dataafect){
        try{
            // afactation customer :
            Customer customeroobject = this.customerrepository.findById(dataafect.id_customer()).orElseThrow(()->new ResourceNotFoundException("customer not found ")) ;
            Opportunity opportunity = this.opportuniterepository.findById(dataafect.id_opportunity()).orElseThrow(()->new ResourceNotFoundException("opportunity dont existe "));
            opportunity.setCustomer(customeroobject);
            this.opportuniterepository.save(opportunity) ;

            return  ResponseEntity.status(HttpStatus.CREATED).body("opportunity affected ");


        }catch(Exception e ){
            // debug
            System.out.println("Problemme dans le server : --> "+e.getMessage());
            throw new RuntimeException("problemme dans le service ");
        }

    }


}
