package com.oualid.CRMSaas.Services.ManagerService;


import com.oualid.CRMSaas.DTO.*;
import com.oualid.CRMSaas.Entity.*;
import com.oualid.CRMSaas.Entity.Enum.LeadSource;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;
import com.oualid.CRMSaas.Entity.Enum.RoleUser;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.*;
import com.oualid.CRMSaas.Services.EmailService;
import com.oualid.CRMSaas.Tools.Email_Commande.SendEmailCommand;
import com.oualid.CRMSaas.Tools.LoggerObserver.EventManager;
import com.oualid.CRMSaas.Tools.PasswordGenerate_Builder.PasswordBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ComercialeMangementService {
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;
    private final EventManager eventmanager ;
    private final EmailService emailservice ;
    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository;
    private final OpportunityRepository opportunityRepository;

    public ComercialeMangementService(UserRepository userrepo
                                        , PasswordEncoder passwordencoder
                                        , EventManager eventManager
                                        ,EmailService emailservice
                                        , LeadRepository leadRepository
                                        , CustomerRepository customerRepository
                                        , OpportunityRepository opportunityRepository
                                      ){
        this.userRepository = userrepo ;
        this.passwordEncoder = passwordencoder ;
        this.eventmanager = eventManager ;
        this.emailservice = emailservice ;
        this.leadRepository = leadRepository;
        this.customerRepository = customerRepository;
        this.opportunityRepository = opportunityRepository;
    }

    public User CreatedNewComerciale(ComercialeRequestDTO datacomerciale , String email ){
        try{
            User manager = this.userRepository.findByEmail(email);
            String generatePassword = new PasswordBuilder()
                    .length(6)
                    .withNumbers(true)
                    .withUppercase(true)
                    .build();

            User new_comerciale = User.builder()
                    .firstName(datacomerciale.firstName())
                    .lastName(datacomerciale.lastName())
                    .email(datacomerciale.email())
                    .company(manager.getCompany())
                    .isActive(true)
                    .role(RoleUser.COMMERCIAL)
                    .password(this.passwordEncoder.encode(generatePassword))
                    .build();

            User user_created = this.userRepository.save(new_comerciale);

            String subject = "Votre compte CRM SaaS a été créé";
            String body =
                    "<html>" +
                            "<body style='font-family: Arial, sans-serif; background-color:#f4f6f9; padding:20px;'>" +
                            "<div style='max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;'>" +
                            "<h2 style='color:#2c3e50;'>Bienvenue sur CRM SaaS</h2>" +
                            "<p>Bonjour <b>" + datacomerciale.firstName() + "</b>,</p>" +
                            "<p>Votre compte commercial a été créé avec succès.</p>" +
                            "<h3 style='color:#2980b9;'>Vos informations de connexion :</h3>" +
                            "<p><b>Email :</b> " + datacomerciale.email() + "</p>" +
                            "<p><b>Mot de passe temporaire :</b> <span style='color:red;'>" + generatePassword + "</span></p>" +
                            "<p style='margin-top:20px;'>Nous vous recommandons de changer votre mot de passe après la première connexion.</p>" +
                            "<hr>" +
                            "<p style='font-size:12px; color:gray;'>L'équipe CRM SaaS</p>" +
                            "</div>" +
                            "</body>" +
                            "</html>";
            SendEmailCommand commande = new SendEmailCommand(
                    this.emailservice ,
                    datacomerciale.email() ,
                    subject ,
                    body
            );
            commande.execute();
            return  user_created;
        }catch(Exception e){
            System.out.println("[EXCEPTION] : "+e.getMessage()) ;
            throw  new RuntimeException("problemme dans le service ");
        }
    }

    public ResponseEntity<String> BloquerCompt(UUID id_comerciale){
        try{
            User comerciale_bloquer = this.userRepository.findById(id_comerciale).orElseThrow(()->new UserNotFoundException("user not found"));
            comerciale_bloquer.setIsActive(false);
            this.userRepository.save(comerciale_bloquer) ;
            AuditLogger NewAuditlogger =  AuditLogger.builder()
                    .type("BOQUER COMPT COMERCIALE")
                    .log("MANAGER BLOQUER COMPT USER ")
                    .build();
            this.eventmanager.publish(NewAuditlogger);
            return ResponseEntity.status(HttpStatus.CREATED).body("compt est bloque");
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }
    }

    public ResponseEntity<String > DebloquerComerciale(UUID id_comerciale){
        try{
            User comerciale = this.userRepository.findById(id_comerciale).orElseThrow(()-> new UserNotFoundException("user not found")) ;
            comerciale.setIsActive(true);
            this.userRepository.save(comerciale);
            AuditLogger NewAuditlogger =  AuditLogger.builder()
                    .type("DEBOQUER COMPT COMERCIALE")
                    .log("MANAGER DEBLOQUER COMPT USER ")
                    .build();
            this.eventmanager.publish(NewAuditlogger);
            return  ResponseEntity.status(HttpStatus.CREATED).body("acount est debloquer");
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }
    }

    public UserResponseDTO showComerciale(UUID id_comerciale ){
        try{
            User comercialeSHow = this.userRepository.findById(id_comerciale).orElseThrow(()->new UserNotFoundException("user not found ")) ;
            return new UserResponseDTO(
                    comercialeSHow.getId(),
                    comercialeSHow.getFirstName() ,
                    comercialeSHow.getLastName() ,
                    comercialeSHow.getEmail() ,
                    comercialeSHow.getIsActive() ,
                    comercialeSHow.getRole()
            );
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }
    }

    public List<UserResponseDTO> getALlcommerciauCompany(String username_manager){
        try{
            User manager = this.userRepository.findByEmail(username_manager);
            List<User> dataUsersResponse = this.userRepository.findByCompany(manager.getCompany());
            List<UserResponseDTO> dataDtoResponseMapping = new ArrayList<>();
            for(User user : dataUsersResponse){
                UserResponseDTO userresponsedto = new UserResponseDTO(
                        user.getId(),
                       user.getFirstName() ,
                       user.getLastName() ,
                       user.getEmail() ,
                       user.getIsActive() ,
                       user.getRole()
                );
                dataDtoResponseMapping.add(userresponsedto);
            }
            return dataDtoResponseMapping ;
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("problemme dans le service ") ;
        }
    }

    @Transactional
    public ResponseEntity<String> DeleteComerciale(UUID id){
        try{
            User comercial = this.userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Commercial not found"));
            this.userRepository.delete(comercial);
            AuditLogger logger = AuditLogger.builder()
                    .type("DELETE COMERCIALE")
                    .log("MANAGER DELETE COMMERCIAL USER " + comercial.getEmail())
                    .build();
            this.eventmanager.publish(logger);
            return ResponseEntity.ok("Commercial supprimé avec succès");
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de la suppression du commercial");
        }
    }

    public List<LeadResponseDTO> getCommercialLeads(UUID commercialId){
        try{
            User commercial = this.userRepository.findById(commercialId)
                    .orElseThrow(() -> new UserNotFoundException("Commercial not found"));
            List<Lead> leads = this.leadRepository.findAllByAssignedTo(commercial);
            return leads.stream().map(lead -> new LeadResponseDTO(
                    lead.getId(),
                    lead.getFirstname(),
                    lead.getLastname(),
                    lead.getEmail(),
                    lead.getPhone(),
                    lead.getSource(),
                    lead.getStatus(),
                    lead.getCreatedAt(),
                    lead.getUpdatedAt()
            )).toList();
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de la récupération des leads");
        }
    }

    public List<CustomerResponseDTO> getCommercialCustomers(UUID commercialId){
        try{
            User commercial = this.userRepository.findById(commercialId)
                    .orElseThrow(() -> new UserNotFoundException("Commercial not found"));
            List<Customer> customers = this.customerRepository.findAllByCreatedBy(commercial);
            return customers.stream().map(c -> new CustomerResponseDTO(
                    c.getId(),
                    c.getName(),
                    c.getEmail(),
                    c.getPhone(),
                    c.getWebsite(),
                    c.getAddres(),
                    c.getCreatedAt(),
                    c.getUpdatedAt()
            )).toList();
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de la récupération des clients");
        }
    }

    @Transactional
    public ResponseEntity<DistributeResponseDTO> ImportComerciales(
            ImportComercialeRequestDTO request,
            String managerEmail
    ){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            int count = 0;
            for(ComercialeRequestDTO dto : request.commercials()){
                String generatePassword = new PasswordBuilder()
                        .length(6)
                        .withNumbers(true)
                        .withUppercase(true)
                        .build();

                User newComercial = User.builder()
                        .firstName(dto.firstName())
                        .lastName(dto.lastName())
                        .email(dto.email())
                        .company(manager.getCompany())
                        .isActive(true)
                        .role(RoleUser.COMMERCIAL)
                        .password(this.passwordEncoder.encode(generatePassword))
                        .build();

                this.userRepository.save(newComercial);

                if(request.sendEmail()){
                    String subject = "Votre compte CRM SaaS a été créé";
                    String body = "<html><body style='font-family: Arial, sans-serif; background-color:#f4f6f9; padding:20px;'>" +
                            "<div style='max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px;'>" +
                            "<h2 style='color:#2c3e50;'>Bienvenue sur CRM SaaS</h2>" +
                            "<p>Bonjour <b>" + dto.firstName() + "</b>,</p>" +
                            "<p>Votre compte commercial a été créé avec succès.</p>" +
                            "<h3 style='color:#2980b9;'>Vos informations de connexion :</h3>" +
                            "<p><b>Email :</b> " + dto.email() + "</p>" +
                            "<p><b>Mot de passe temporaire :</b> <span style='color:red;'>" + generatePassword + "</span></p>" +
                            "<p style='margin-top:20px;'>Nous vous recommandons de changer votre mot de passe après la première connexion.</p>" +
                            "<hr><p style='font-size:12px; color:gray;'>L'équipe CRM SaaS</p></div></body></html>";
                    SendEmailCommand cmd = new SendEmailCommand(this.emailservice, dto.email(), subject, body);
                    cmd.execute();
                }
                count++;
            }
            String msg = count + " commercial(aux) importé(s) avec succès" +
                    (request.sendEmail() ? " (email envoyé)" : "");
            return ResponseEntity.ok(new DistributeResponseDTO(msg, count, count));
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de l'import des commerciaux");
        }
    }

    @Transactional
    public ResponseEntity<DistributeResponseDTO> ImportLeadsAndDistribute(
            ImportLeadDistributeDTO request,
            String managerEmail
    ){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            List<User> commercials = this.userRepository.findByCompany(manager.getCompany())
                    .stream()
                    .filter(u -> u.getRole() == RoleUser.COMMERCIAL && u.getIsActive())
                    .toList();

            if(commercials.isEmpty()){
                return ResponseEntity.badRequest()
                        .body(new DistributeResponseDTO("Aucun commercial actif dans votre entreprise", 0, 0));
            }

            int count = 0;
            Random random = new Random();
            for(LeadImportDTO dto : request.leads()){
                User assignedTo = request.distributeRandomly()
                        ? commercials.get(random.nextInt(commercials.size()))
                        : null;

                Lead lead = Lead.builder()
                        .firstname(dto.firstname())
                        .lastname(dto.lastname())
                        .email(dto.email())
                        .phone(dto.phone())
                        .source(dto.source() != null ? dto.source() : LeadSource.IMPORT)
                        .status(LeadStatus.NEW)
                        .company(manager.getCompany())
                        .assignedTo(assignedTo)
                        .build();

                this.leadRepository.save(lead);
                count++;
            }

            String msg = count + " lead(s) importé(s)" +
                    (request.distributeRandomly() ? " et répartis sur " + commercials.size() + " commercial(aux)" : "");
            return ResponseEntity.ok(new DistributeResponseDTO(msg, count, commercials.size()));
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de l'import des leads");
        }
    }

    public List<CustomerResponseDTO> getAllCompanyCustomers(String managerEmail){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            List<User> companyUsers = this.userRepository.findByCompany(manager.getCompany());
            List<Customer> customers = this.customerRepository.findAllByCreatedByIn(companyUsers);
            return customers.stream().map(c -> new CustomerResponseDTO(
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getWebsite(), c.getAddres(), c.getCreatedAt(), c.getUpdatedAt()
            )).toList();
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de la récupération des clients");
        }
    }

    @Transactional
    public ResponseEntity<DistributeResponseDTO> DistributeCustomers(String managerEmail){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            List<User> companyUsers = this.userRepository.findByCompany(manager.getCompany());
            List<User> commercials = companyUsers.stream()
                    .filter(u -> u.getRole() == RoleUser.COMMERCIAL && u.getIsActive())
                    .toList();

            if(commercials.isEmpty()){
                return ResponseEntity.badRequest()
                        .body(new DistributeResponseDTO("Aucun commercial actif", 0, 0));
            }

            List<Customer> allCompanyCustomers = this.customerRepository.findAllByCreatedByIn(companyUsers);

            if(allCompanyCustomers.isEmpty()){
                return ResponseEntity.ok(new DistributeResponseDTO("Aucun client trouvé dans l'entreprise", 0, 0));
            }

            Random random = new Random();
            int count = 0;
            for(Customer customer : allCompanyCustomers){
                User assigned = commercials.get(random.nextInt(commercials.size()));
                customer.setCreatedBy(assigned);
                this.customerRepository.save(customer);
                count++;
            }

            return ResponseEntity.ok(new DistributeResponseDTO(
                    count + " client(s) redistribué(s) sur " + commercials.size() + " commercial(aux)",
                    count,
                    commercials.size()
            ));
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de la distribution des clients");
        }
    }

    @Transactional
    public ResponseEntity<DistributeResponseDTO> DistributeOpportunities(String managerEmail){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            List<User> companyUsers = this.userRepository.findByCompany(manager.getCompany());
            List<User> commercials = companyUsers.stream()
                    .filter(u -> u.getRole() == RoleUser.COMMERCIAL && u.getIsActive())
                    .toList();

            if(commercials.isEmpty()){
                return ResponseEntity.badRequest()
                        .body(new DistributeResponseDTO("Aucun commercial actif", 0, 0));
            }

            List<Opportunity> allCompanyOpps = this.opportunityRepository.findByAssignedToIn(companyUsers);

            if(allCompanyOpps.isEmpty()){
                return ResponseEntity.ok(new DistributeResponseDTO("Aucune opportunité trouvée dans l'entreprise", 0, 0));
            }

            Random random = new Random();
            int count = 0;
            for(Opportunity opp : allCompanyOpps){
                User assigned = commercials.get(random.nextInt(commercials.size()));
                opp.setAssignedTo(assigned);
                this.opportunityRepository.save(opp);
                count++;
            }

            return ResponseEntity.ok(new DistributeResponseDTO(
                    count + " opportunité(s) redistribuée(s) sur " + commercials.size() + " commercial(aux)",
                    count,
                    commercials.size()
            ));
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de la distribution des opportunités");
        }
    }

    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequest dto, String managerEmail){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            Customer customer = Customer.builder()
                    .name(dto.name())
                    .email(dto.email())
                    .phone(dto.phone())
                    .website(dto.website())
                    .addres(dto.adress())
                    .createdBy(manager)
                    .build();
            this.customerRepository.save(customer);
            return new CustomerResponseDTO(
                    customer.getId(), customer.getName(), customer.getEmail(),
                    customer.getPhone(), customer.getWebsite(), customer.getAddres(),
                    customer.getCreatedAt(), customer.getUpdatedAt()
            );
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de la création du client");
        }
    }

    @Transactional
    public ImportResultDTO importCustomersFromCsv(String managerEmail, MultipartFile file){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            int count = 0;
            int errors = 0;
            try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))){
                String headerLine = br.readLine();
                if(headerLine == null){
                    return new ImportResultDTO(0, 0, "Fichier vide");
                }
                String[] headers = headerLine.split(",");
                // find column indices
                int nameIdx = -1, emailIdx = -1, phoneIdx = -1, websiteIdx = -1, addressIdx = -1;
                for(int i = 0; i < headers.length; i++){
                    String h = headers[i].trim().toLowerCase().replaceAll("[\\s\\-]", "");
                    if(h.equals("name") || h.equals("nom")) nameIdx = i;
                    if(h.equals("email") || h.equals("e-mail") || h.equals("courriel")) emailIdx = i;
                    if(h.equals("phone") || h.equals("telephone") || h.equals("tel") || h.equals("téléphone")) phoneIdx = i;
                    if(h.equals("website") || h.equals("site") || h.equals("siteweb") || h.equals("siteweb") || h.equals("sitesweb")) websiteIdx = i;
                    if(h.equals("address") || h.equals("adresse") || h.equals("addres")) addressIdx = i;
                }
                if(nameIdx == -1 || emailIdx == -1){
                    return new ImportResultDTO(0, 0, "Colonnes obligatoires manquantes: name, email");
                }
                String line;
                while((line = br.readLine()) != null){
                    try{
                        if(line.trim().isEmpty()) continue;
                        String[] cols = line.split(",");
                        String name = cols[nameIdx].trim();
                        String email = cols.length > emailIdx ? cols[emailIdx].trim() : "";
                        if(name.isEmpty() || email.isEmpty()) continue;
                        String phone = cols.length > phoneIdx ? cols[phoneIdx].trim() : "";
                        String website = cols.length > websiteIdx ? cols[websiteIdx].trim() : "";
                        String address = cols.length > addressIdx ? cols[addressIdx].trim() : "";
                        Customer customer = Customer.builder()
                                .name(name).email(email).phone(phone)
                                .website(website).addres(address)
                                .createdBy(manager).build();
                        this.customerRepository.save(customer);
                        count++;
                    }catch(Exception ex){
                        errors++;
                    }
                }
            }
            String msg = count + " client(s) importé(s) avec succès";
            if(errors > 0) msg += " (" + errors + " ligne(s) ignorée(s))";
            return new ImportResultDTO(count, errors, msg);
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de l'import CSV");
        }
    }

    public ResponseEntity<byte[]> exportCustomersToCsv(String managerEmail){
        try{
            User manager = this.userRepository.findByEmail(managerEmail);
            List<User> companyUsers = this.userRepository.findByCompany(manager.getCompany());
            List<Customer> customers = this.customerRepository.findAllByCreatedByIn(companyUsers);
            StringBuilder sb = new StringBuilder();
            sb.append("name,email,phone,website,address,createdAt\n");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for(Customer c : customers){
                sb.append(escapeCsv(c.getName())).append(",");
                sb.append(escapeCsv(c.getEmail())).append(",");
                sb.append(escapeCsv(c.getPhone())).append(",");
                sb.append(escapeCsv(c.getWebsite())).append(",");
                sb.append(escapeCsv(c.getAddres())).append(",");
                sb.append(c.getCreatedAt() != null ? c.getCreatedAt().format(dtf) : "").append("\n");
            }
            byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "clients_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage()) ;
            throw new RuntimeException("Erreur lors de l'export CSV");
        }
    }

    private String escapeCsv(String value){
        if(value == null) return "";
        if(value.contains(",") || value.contains("\"") || value.contains("\n")){
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
