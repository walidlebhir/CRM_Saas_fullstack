package com.oualid.CRMSaas.Services.ComercialeService;


import com.oualid.CRMSaas.DTO.AffectOpportunityRequest;
import com.oualid.CRMSaas.DTO.OpportuniteRequest;
import com.oualid.CRMSaas.DTO.OpportunityResponseDTO;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;
import com.oualid.CRMSaas.Entity.Opportunity;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.ResourceNotFoundException;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.CustomerRepository;
import com.oualid.CRMSaas.Repository.OpportunityRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    @Transactional
    public OpportunityResponseDTO CreateNewOpportunite(OpportuniteRequest dataopertunity  , String comerciale_username){

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
            return toDto(opportunityCreated);
        }catch(Exception e){
            // debug
            System.out.println("Problemme dans le service "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }

    }

    @Transactional
    public List<OpportunityResponseDTO> importOpportunitiesFromCsv(MultipartFile file, String username) {
        try {
            User commerciale = this.userrepository.findByEmail(username);
            if (commerciale == null) {
                throw new UserNotFoundException("commerciale not found");
            }

            List<Opportunity> saved = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new RuntimeException("Fichier CSV vide");
            }

            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty()) continue;

                String[] fields = parseCsvLine(line);
                if (fields.length < 3) {
                    System.out.println("Ligne " + lineNum + " ignorée: nombre de champs insuffisant");
                    continue;
                }

                String title = fields[0].trim();
                BigDecimal amount = BigDecimal.ZERO;
                OpportunityStage stage = OpportunityStage.PROSPECTING;
                Integer probability = 0;
                String description = "";

                try {
                    if (fields.length > 1 && !fields[1].trim().isEmpty()) {
                        amount = new BigDecimal(fields[1].trim());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ligne " + lineNum + ": montant invalide '" + fields[1] + "'");
                }

                if (fields.length > 2 && !fields[2].trim().isEmpty()) {
                    try {
                        stage = OpportunityStage.valueOf(fields[2].trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ligne " + lineNum + ": étape invalide '" + fields[2] + "'");
                    }
                }

                if (fields.length > 3 && !fields[3].trim().isEmpty()) {
                    try {
                        probability = Integer.parseInt(fields[3].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Ligne " + lineNum + ": probabilité invalide '" + fields[3] + "'");
                    }
                }

                if (fields.length > 4) {
                    description = fields[4].trim();
                }

                Opportunity opportunity = Opportunity.builder()
                        .title(title)
                        .amount(amount)
                        .stage(stage)
                        .probabilirty(probability)
                        .description(description)
                        .assignedTo(commerciale)
                        .build();

                saved.add(this.opportuniterepository.save(opportunity));
            }

            reader.close();
            return saved.stream().map(this::toDto).toList();
        } catch (Exception e) {
            System.out.println("Problème dans l'import CSV: " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'import CSV: " + e.getMessage());
        }
    }

    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }

    public List<OpportunityStage> getAllStageOpportunite(){
        return  List.of(OpportunityStage.values()) ;
    }

    @Transactional(readOnly = true)
    public List<OpportunityResponseDTO> ListAllOpportunity(String user_name){
        try{
            User comerciale = this.userrepository.findByEmail(user_name) ;
            if(comerciale == null){
                throw  new UserNotFoundException("commerciale not found") ;
            }

            List<Opportunity> dataopportunity = this.opportuniterepository.findByAssignedTo(comerciale);
            return dataopportunity.stream().map(this::toDto).toList();

        }catch(Exception e){
            System.out.println("Problemme dans le service "+e.getMessage());
            throw new RuntimeException("problemme dans le service ") ;
        }
    }

    private OpportunityResponseDTO toDto(Opportunity opp) {
        OpportunityResponseDTO.CustomerInfo customerInfo = null;
        if (opp.getCustomer() != null) {
            customerInfo = new OpportunityResponseDTO.CustomerInfo(
                opp.getCustomer().getId(),
                opp.getCustomer().getName(),
                opp.getCustomer().getEmail()
            );
        }
        return new OpportunityResponseDTO(
            opp.getId(),
            opp.getTitle(),
            opp.getAmount(),
            opp.getStage(),
            opp.getProbabilirty(),
            opp.getDescription(),
            opp.getCreatedAt(),
            opp.getUpdatedAt(),
            customerInfo
        );
    }

    @Transactional(readOnly = true)
    public OpportunityResponseDTO ShowOpportunity(Long opportunity_id){
        try{
            Opportunity opportunity = this.opportuniterepository.findById(opportunity_id).orElseThrow(()->new ResourceNotFoundException("this opportunity dont existe "));
            return toDto(opportunity);
        }catch(Exception e){
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

    @Transactional
    public OpportunityResponseDTO UpdateOpportunity(
            Long id_opportunity ,
            OpportuniteRequest dataOpportunityUpdate
    ){
        try{
            Opportunity opportunityUpdate = this.opportuniterepository.findById(id_opportunity).orElseThrow(()->new ResourceNotFoundException("opportunity ot found "));
            opportunityUpdate.setTitle(dataOpportunityUpdate.title());
            opportunityUpdate.setStage(dataOpportunityUpdate.stage());
            opportunityUpdate.setAmount(dataOpportunityUpdate.amount());
            opportunityUpdate.setProbabilirty(dataOpportunityUpdate.probability());
            opportunityUpdate.setDescription(dataOpportunityUpdate.description());
            Opportunity saved = this.opportuniterepository.save(opportunityUpdate);
            return toDto(saved);

        }catch(Exception e){
            System.out.println("Probleme dans le service "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ") ;
        }
    }


}
