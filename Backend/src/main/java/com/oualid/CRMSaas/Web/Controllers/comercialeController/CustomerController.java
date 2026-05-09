package com.oualid.CRMSaas.Web.Controllers.comercialeController;


import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Services.ComercialeService.CustomerManageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comerciale")
public class CustomerController {

    private final CustomerManageService customerservice ;

    public CustomerController(CustomerManageService customerservice){
        this.customerservice = customerservice ;
    }

    @GetMapping("/get/all/customers")
    public List<Customer> getALlCustomer(@AuthenticationPrincipal UserDetails userDetails){
        return  this.customerservice.getALlCustomer(userDetails.getUsername());

    }

    @GetMapping("/show/customer/{id}")
    public Customer ShowCustomer(@PathVariable UUID id ){
        return  this.customerservice.ShowCustomer(id);
    }

    @DeleteMapping("/delete/customer/{id}")
    public ResponseEntity<String> deletCustomer(@PathVariable UUID id ){
        return this.customerservice.deletCustomer(id);
    }
}
