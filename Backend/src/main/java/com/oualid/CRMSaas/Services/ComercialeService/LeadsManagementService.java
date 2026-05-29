package com.oualid.CRMSaas.Services.ComercialeService;


import com.oualid.CRMSaas.DTO.LeadDTO;
import com.oualid.CRMSaas.DTO.StatusRequest;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.Enum.LeadSource;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;
import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;
import com.oualid.CRMSaas.Entity.Lead;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.ResourceNotFoundException;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.CustomerRepository;
import com.oualid.CRMSaas.Repository.LeadRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
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

    public List<LeadSource> getAllLeadSource(){
        return  List.of(LeadSource.values()) ;
    }

    public List<LeadStatus> getAllLeadStatus(){
        return  List.of(LeadStatus.values()) ;
    }

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
                    .source(dataleadDto.source())
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


    @Transactional(readOnly = true)
    public Page<Lead> getLeadsPaginated(String username, int page, int size) {
        User user = userrepository.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new UserNotFoundException("Utilisateur non trouvé : " + username);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        try {
            return leadrepository.findByAssignedTo(user, pageable);
        } catch (Exception e) {
            log.error("Erreur pagination leads : {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération paginée des leads : " + e.getMessage());
        }
    }

    public List<Lead> getAllLeads(String username_comerciale){
        try{
            // recuperation comerciale :
            User comerciale = this.userrepository.findByEmail(username_comerciale);
            if(comerciale == null){
                throw new UserNotFoundException("comerciale not found ");
            }
            List<Lead> leadsData = this.leadrepository.findAllByAssignedTo(comerciale);
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


    public List<Lead> importLeadsFromCsv(String username, MultipartFile file) {
        List<Lead> created = new ArrayList<>();
        User user = userrepository.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new UserNotFoundException("Utilisateur non trouvé : " + username);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null) {
                throw new RuntimeException("Le fichier CSV est vide");
            }
            String[] columns = header.split(",");
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (line.isBlank()) continue;
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                LeadDTO data = parseCsvLine(columns, values);
                if (data.firstname() == null || data.firstname().isBlank()) {
                    log.warn("Ligne {} ignorée : prénom manquant", lineNum);
                    continue;
                }
                Lead lead = Lead.builder()
                        .firstname(data.firstname())
                        .lastname(data.lastname())
                        .email(data.email())
                        .phone(data.phone())
                        .source(data.source() != null ? data.source() : LeadSource.MANUAL)
                        .status(data.status() != null ? data.status() : LeadStatus.NEW)
                        .company(user.getCompany())
                        .assignedTo(user)
                        .build();
                created.add(leadrepository.save(lead));
            }
            log.info("{} leads importés depuis CSV par {}", created.size(), username);
        } catch (Exception e) {
            log.error("Erreur lors de l'import CSV : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'import CSV : " + e.getMessage());
        }
        return created;
    }

    private LeadDTO parseCsvLine(String[] columns, String[] values) {
        String firstname = null, lastname = null, email = null, phone = null;
        LeadSource source = null;
        LeadStatus status = null;
        for (int i = 0; i < columns.length && i < values.length; i++) {
            String col = columns[i].trim().toLowerCase().replace("\"", "");
            String val = values[i].trim().replace("\"", "");
            switch (col) {
                case "firstname", "prénom", "prenom" -> firstname = val;
                case "lastname", "nom" -> lastname = val;
                case "email", "e-mail", "courriel" -> email = val;
                case "phone", "telephone", "téléphone", "tel" -> phone = val;
                case "source" -> {
                    try { source = LeadSource.valueOf(val.toUpperCase()); } catch (IllegalArgumentException ignored) {}
                }
                case "status", "statut" -> {
                    try { status = LeadStatus.valueOf(val.toUpperCase()); } catch (IllegalArgumentException ignored) {}
                }
            }
        }
        return new LeadDTO(firstname, lastname, email, phone, source, status);
    }

    public Lead UpdateLead(UUID id_lead , LeadDTO dataupdatelead  ){
        try{
            // modeficatio de lead :
            Lead objectLead = this.leadrepository.findById(id_lead).orElseThrow(()->new ResourceNotFoundException("lead not found "));
            objectLead.setSource(dataupdatelead.source());
            objectLead.setFirstname(dataupdatelead.firstname());
            objectLead.setLastname(dataupdatelead.lastname());
            objectLead.setPhone(dataupdatelead.phone());
            objectLead.setEmail(dataupdatelead.email());
            objectLead.setStatus(dataupdatelead.status());

            return  this.leadrepository.save(objectLead);

        }catch(Exception e ){
            // debug
            System.out.println("problemme dans le service "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");
        }

    }



}
