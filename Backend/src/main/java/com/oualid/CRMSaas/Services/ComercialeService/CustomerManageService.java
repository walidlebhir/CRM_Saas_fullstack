package com.oualid.CRMSaas.Services.ComercialeService;


import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.ResourceNotFoundException;
import com.oualid.CRMSaas.Repository.CustomerRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerManageService {

    private final CustomerRepository customerRepo ;
    private  final UserRepository userrepository ;

    public CustomerManageService(CustomerRepository customerrepo ,
                                   UserRepository userrepo){
        this.customerRepo = customerrepo ;
        this.userrepository = userrepo ;
    }


    public List<Customer> getALlCustomer(String username_comerciale ){
        try{
            User comerciale = this.userrepository.findByEmail(username_comerciale);
            List<Customer> dataCustomer = this.customerRepo.findByCreatedBy(comerciale);
            return  dataCustomer ;
        }catch(Exception e){
            System.out.println("[LOG] : "+e.getMessage());
            throw new RuntimeException("problemme dans le service customers");

        }
    }

    public Customer ShowCustomer(UUID id_customer ){
        try{
            Customer customer = this.customerRepo.findById(id_customer).orElseThrow(()->new ResourceNotFoundException("customer dont existe "));
            return  customer  ;
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
}
