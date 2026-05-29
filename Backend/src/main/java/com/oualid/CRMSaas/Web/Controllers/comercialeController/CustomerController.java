package com.oualid.CRMSaas.Web.Controllers.comercialeController;


import com.oualid.CRMSaas.DTO.CustomerDetailResponseDTO;
import com.oualid.CRMSaas.DTO.CustomerRequest;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Services.ComercialeService.CustomerManageService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comerciale")
public class CustomerController {

    private final CustomerManageService customerservice ;

    public CustomerController(CustomerManageService customerservice){
        this.customerservice = customerservice ;
    }


    @PostMapping("/Create/new/Customer")
    public Customer CreateNewCustomer(
            @AuthenticationPrincipal UserDetails userconected ,
            @RequestBody CustomerRequest dataRequestCustomer
            ){
        return  this.customerservice.CreateNewCostumer(dataRequestCustomer , userconected.getUsername());


    }


    @GetMapping("/get/all/customers")
    public List<Customer> getALlCustomer(@AuthenticationPrincipal UserDetails userDetails){
        return  this.customerservice.getALlCustomer(userDetails.getUsername());
    }

    @GetMapping("/get/all/customers/page")
    public Page<Customer> getCustomersPaginated(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String search
    ) {
        return customerservice.getCustomersPaginated(userDetails.getUsername(), page, size, search);
    }

    @GetMapping("/show/customer/{id}")
    public CustomerDetailResponseDTO ShowCustomer(@PathVariable UUID id ){
        return  this.customerservice.ShowCustomer(id);
    }

    @DeleteMapping("/delete/customer/{id}")
    public ResponseEntity<String> deletCustomer(@PathVariable UUID id ){
        return this.customerservice.deletCustomer(id);
    }

    @PatchMapping("/update/data/customer/{id_customer}")
    public Customer UpdateCustomer(@PathVariable UUID id_customer , @RequestBody CustomerRequest dataCustomer){
        return  this.customerservice.UpdateCustomer(id_customer , dataCustomer);
    }

    @GetMapping("/export/customer/csv")
    public ResponseEntity<byte[]> exportCustomersCsv(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return customerservice.exportCustomersToCsv(userDetails.getUsername());
    }

    @PostMapping(value = "/import/customer/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Customer> importCustomersCsv(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        return customerservice.importCustomersFromCsv(userDetails.getUsername(), file);
    }
}
