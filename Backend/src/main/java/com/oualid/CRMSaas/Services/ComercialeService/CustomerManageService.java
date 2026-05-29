package com.oualid.CRMSaas.Services.ComercialeService;


import com.oualid.CRMSaas.DTO.CustomerDetailResponseDTO;
import com.oualid.CRMSaas.DTO.CustomerRequest;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.ResourceNotFoundException;
import com.oualid.CRMSaas.Repository.CustomerRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.UUID;

@Slf4j
@Service
public class CustomerManageService {

    private final CustomerRepository customerRepo ;
    private  final UserRepository userrepository ;

    public CustomerManageService(CustomerRepository customerrepo ,
                                   UserRepository userrepo){
        this.customerRepo = customerrepo ;
        this.userrepository = userrepo ;
    }

    // ajouter customer :

    public Customer CreateNewCostumer(CustomerRequest dataCustomer , String username ){
        try{
            User comerciale = this.userrepository.findByEmail(username);
            Customer NewCustmer = Customer.builder()
                    .name(dataCustomer.name())
                    .email(dataCustomer.email())
                    .phone(dataCustomer.phone())
                    .addres(dataCustomer.adress())
                    .website(dataCustomer.website())
                    .createdBy(comerciale)
                    .build();

            return  this.customerRepo.save(NewCustmer) ;
        }catch(Exception e){
            // debug
            System.out.println("problemme dans le service "+e.getMessage());
            throw new RuntimeException("problemme dans le service ");

        }
    }


    @Transactional(readOnly = true)
    public Page<Customer> getCustomersPaginated(String username, int page, int size, String search) {
        User user = userrepository.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new RuntimeException("Utilisateur non trouvé : " + username);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (search != null && !search.isBlank()) {
            return customerRepo.findByCreatedByAndSearch(user, search, pageable);
        }
        return customerRepo.findByCreatedBy(user, pageable);
    }

    public List<Customer> importCustomersFromCsv(String username, MultipartFile file) {
        List<Customer> created = new ArrayList<>();
        User user = userrepository.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new RuntimeException("Utilisateur non trouvé : " + username);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null) throw new RuntimeException("Le fichier CSV est vide");
            String[] columns = header.split(",");
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (line.isBlank()) continue;
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                String name = null, email = null, phone = null, website = null, address = null;
                for (int i = 0; i < columns.length && i < values.length; i++) {
                    String col = columns[i].trim().toLowerCase().replace("\"", "");
                    String val = values[i].trim().replace("\"", "");
                    switch (col) {
                        case "name", "nom" -> name = val;
                        case "email", "e-mail", "courriel" -> email = val;
                        case "phone", "telephone", "téléphone", "tel" -> phone = val;
                        case "website", "site", "site web", "siteweb" -> website = val;
                        case "address", "adresse", "addres" -> address = val;
                    }
                }
                if (name == null || name.isBlank()) {
                    log.warn("Ligne {} ignorée : nom manquant", lineNum);
                    continue;
                }
                Customer customer = Customer.builder()
                        .name(name)
                        .email(email)
                        .phone(phone)
                        .website(website)
                        .addres(address)
                        .createdBy(user)
                        .build();
                created.add(customerRepo.save(customer));
            }
            log.info("{} clients importés depuis CSV par {}", created.size(), username);
        } catch (Exception e) {
            log.error("Erreur lors de l'import CSV clients : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de l'import CSV : " + e.getMessage());
        }
        return created;
    }

    public List<Customer> getALlCustomer(String username_comerciale ){
        try{
            User comerciale = this.userrepository.findByEmail(username_comerciale);
            List<Customer> dataCustomer = this.customerRepo.findAllByCreatedBy(comerciale);
            return  dataCustomer ;
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage());
            throw new RuntimeException("problemme dans le service customers");

        }
    }

    @Transactional(readOnly = true)
    public CustomerDetailResponseDTO ShowCustomer(UUID id_customer){
        try{
            Customer customer = this.customerRepo.findById(id_customer).orElseThrow(()->new ResourceNotFoundException("customer dont existe "));
            List<CustomerDetailResponseDTO.OpportunitySummary> oppSummaries = customer.getOpportunities().stream()
                .map(opp -> new CustomerDetailResponseDTO.OpportunitySummary(
                    opp.getId(),
                    opp.getTitle(),
                    opp.getAmount(),
                    opp.getStage(),
                    opp.getProbabilirty(),
                    opp.getDescription(),
                    opp.getCreatedAt(),
                    opp.getUpdatedAt()
                ))
                .toList();
            return new CustomerDetailResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getWebsite(),
                customer.getAddres(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                oppSummaries
            );
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage());
            throw new RuntimeException("problemme dans le service customers");

        }
    }

    public ResponseEntity<String> deletCustomer(UUID id_customer ){
        try{
            this.customerRepo.deleteById(id_customer);
            return  ResponseEntity.status(HttpStatus.CREATED).body("customer delete with succes");

        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage());
            throw new RuntimeException("problemme dans le service customers");

        }
    }


    public ResponseEntity<byte[]> exportCustomersToCsv(String username) {
        User user = userrepository.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new RuntimeException("Utilisateur non trouvé : " + username);
        }
        List<Customer> customers = customerRepo.findAllByCreatedBy(user);
        StringBuilder csv = new StringBuilder();
        csv.append("Name,Email,Phone,Website,Address,Created Date\n");
        for (Customer c : customers) {
            csv.append(escapeCsv(c.getName())).append(",")
               .append(escapeCsv(c.getEmail())).append(",")
               .append(escapeCsv(c.getPhone())).append(",")
               .append(escapeCsv(c.getWebsite())).append(",")
               .append(escapeCsv(c.getAddres())).append(",")
               .append(c.getCreatedAt() != null ? c.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE) : "")
               .append("\n");
        }
        byte[] bytes = csv.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customers_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv");
        log.info("{} customers exported to CSV by {}", customers.size(), username);
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public Customer UpdateCustomer(UUID id_customer , CustomerRequest dataupdateCustomer){
        try{
            Customer customerupdate = this.customerRepo.findById(id_customer).orElseThrow(()->new ResourceNotFoundException("customer not found"));
            customerupdate.setName(dataupdateCustomer.name());
            customerupdate.setEmail(dataupdateCustomer.email());
            customerupdate.setPhone(dataupdateCustomer.phone());
            customerupdate.setWebsite(dataupdateCustomer.website());
            customerupdate.setAddres(dataupdateCustomer.adress());
            return  this.customerRepo.save(customerupdate) ;
        }catch(Exception e){
            // debug
            System.out.println("Problemme dans le service "+e.getMessage());
            throw  new RuntimeException("problemme dans le service ");

        }

    }
}
