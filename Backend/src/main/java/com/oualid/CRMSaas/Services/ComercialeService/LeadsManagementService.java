package com.oualid.CRMSaas.Services.ComercialeService;


import com.oualid.CRMSaas.DTO.LeadDTO;
import com.oualid.CRMSaas.DTO.StatusRequest;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;
import com.oualid.CRMSaas.Entity.Lead;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.ResourceNotFoundException;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.CustomerRepository;
import com.oualid.CRMSaas.Repository.LeadRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LeadsManagementService {

    private  final LeadRepository leadrepository ;
    private final CustomerRepository customerRepo ;
    private  final UserRepository userrepository ;

    public LeadsManagementService(LeadRepository leadrepo
                                    , CustomerRepository customerrepo ,
                                    UserRepository userrepo
    ){

        this.customerRepo = customerrepo ;
        this.leadrepository = leadrepo ;
        this.userrepository = userrepo;

    }

    /**
     * create lead manuel
     * modefie status
     * transformet lead en customer
     */

    public Lead CreatedNewLead(LeadDTO dataleadDto , String username_comerciale){
        try{

            // recuperation comerciale :
            User comerciale = this.userrepository.findByEmail(username_comerciale);
            if(comerciale == null){
                throw  new UserNotFoundException("comerciale not found ");
            }
            Lead newLead = Lead.builder()
                    .firstname(dataleadDto.firstname())
                    .lastname(dataleadDto.lastname())
                    .email(dataleadDto.email())
                    .phone(dataleadDto.phone())
                    .status(LeadStatus.NEW)
                    .company(comerciale.getCompany())
                    .assignedTo(comerciale)
                    .build();
            Lead leadSaved = this.leadrepository.save(newLead);
            return  leadSaved ;
        }catch(Exception e){

            // debug
            System.out.println("[LOG] : "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");
        }

    }


    public ResponseEntity<String> ChangeStatusLead(UUID id_lead , StatusRequest statusrequest ){
        try{
            Lead leadupdated = this.leadrepository.findById(id_lead).orElseThrow(()->new ResourceNotFoundException("lead n'existe pas")) ;
            leadupdated.setStatus(statusrequest.status());
            this.leadrepository.save(leadupdated) ;

            return  ResponseEntity.status(HttpStatus.CREATED).body("lead status updated ");

        }catch(Exception e){
            // debug
            System.out.println("[LOG] : "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");
        }
    }

    // suprimer lead :

    public ResponseEntity<String> DeleteLead(UUID lead_id){
        try{
            this.leadrepository.deleteById(lead_id);
            return  ResponseEntity.status(HttpStatus.CREATED).body("lead delete with succes") ;

        }catch (Exception e ){
            // debug
            System.out.println("[LOG] : "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");
        }

    }

    // transformer lead en Customer
    @Transactional
    public Customer TransformLeadEncustomer(UUID lead_id){
        try{
            Lead leadTransform = this.leadrepository.findById(lead_id).orElseThrow(()-> new ResourceNotFoundException("this lead dont existe "));
            Customer NewCustomer = Customer.builder()
                    .name(leadTransform.getFirstname() + "" + leadTransform.getLastname())
                    .email(leadTransform.getEmail())
                    .phone(leadTransform.getPhone())
                    .createdBy(leadTransform.getAssignedTo())
                    .build();
            Customer customerSaved = this.customerRepo.save(NewCustomer) ;
            // modefie status lead
            leadTransform.setStatus(LeadStatus.WON);
            this.leadrepository.save(leadTransform) ;
            return  customerSaved ;

        }catch(Exception e){
            // debug
            System.out.println("[LOG] : "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");
        }

    }


    public List<Lead> getAllLeads(String username_comerciale){
        try{
            // recuperation comerciale :
            User comerciale = this.userrepository.findByEmail(username_comerciale);
            if(comerciale == null){
                throw new UserNotFoundException("comerciale not found ");
            }
            List<Lead> leadsData = this.leadrepository.findByAssignedTo(comerciale);
            return  leadsData ;
        }catch(Exception e){
            // debug
            System.out.println("[LOG] : "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");
        }

    }

    public Lead ShowLead(UUID id_lead ){
        try{
            Lead leadInfo = this.leadrepository.findById(id_lead).orElseThrow(()->new ResourceNotFoundException("lead not existe "));
            return  leadInfo ;
        }catch(Exception e){
            // debug
            System.out.println("[LOG] : "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");
        }
    }



}
