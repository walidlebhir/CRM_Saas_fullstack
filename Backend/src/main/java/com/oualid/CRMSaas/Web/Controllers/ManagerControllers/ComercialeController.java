package com.oualid.CRMSaas.Web.Controllers.ManagerControllers;


import com.oualid.CRMSaas.DTO.ComercialeRequestDTO;
import com.oualid.CRMSaas.DTO.CustomerRequest;
import com.oualid.CRMSaas.DTO.CustomerResponseDTO;
import com.oualid.CRMSaas.DTO.DistributeResponseDTO;
import com.oualid.CRMSaas.DTO.ImportComercialeRequestDTO;
import com.oualid.CRMSaas.DTO.ImportLeadDistributeDTO;
import com.oualid.CRMSaas.DTO.ImportResultDTO;
import com.oualid.CRMSaas.DTO.LeadResponseDTO;
import com.oualid.CRMSaas.DTO.UserResponseDTO;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Services.ManagerService.ComercialeMangementService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/manager")
public class ComercialeController {

    private ComercialeMangementService comercialeService ;

    public ComercialeController(ComercialeMangementService comercialeservice ){
        this.comercialeService = comercialeservice  ;
    }

    @PostMapping("/create/comerciale")
    public User CreateNewComerciale(@RequestBody ComercialeRequestDTO comerciale , @AuthenticationPrincipal UserDetails userdetails ){
        return  this.comercialeService.CreatedNewComerciale(comerciale , userdetails.getUsername());
    }

    @PostMapping("/bloquer/compt/{id_com}")
    public ResponseEntity<String> BloquerAcountComerciale(@PathVariable UUID id_com){
        return  this.comercialeService.BloquerCompt(id_com);
    }

    @PostMapping("/debloquer/compt/{id_com}")
    public ResponseEntity<String> deBloquerAcountComerciale(@PathVariable UUID id_com){
        return  this.comercialeService.DebloquerComerciale(id_com);
    }


    @GetMapping("/show/comerciale/{id}")
    public UserResponseDTO ShowCOmerciale(@PathVariable UUID id){
        return  this.comercialeService.showComerciale(id);
    }

    @GetMapping("/show/comerciale/{id}/leads")
    public List<LeadResponseDTO> GetCommercialLeads(@PathVariable UUID id){
        return this.comercialeService.getCommercialLeads(id);
    }

    @GetMapping("/show/comerciale/{id}/customers")
    public List<CustomerResponseDTO> GetCommercialCustomers(@PathVariable UUID id){
        return this.comercialeService.getCommercialCustomers(id);
    }

    @GetMapping("/show/all/comerciale")
    public List<UserResponseDTO > getAllcomecriales(@AuthenticationPrincipal UserDetails manager_conect){
        return  this.comercialeService.getALlcommerciauCompany(manager_conect.getUsername());

    }

    @DeleteMapping("/delete/comerciale/{id}")
    public ResponseEntity<String> DeleteComerciale(@PathVariable UUID id){
        return this.comercialeService.DeleteComerciale(id);
    }

    @PostMapping("/import/comerciales")
    public ResponseEntity<DistributeResponseDTO> ImportComerciales(
            @RequestBody ImportComercialeRequestDTO request,
            @AuthenticationPrincipal UserDetails userdetails
    ){
        return this.comercialeService.ImportComerciales(request, userdetails.getUsername());
    }

    @PostMapping("/leads/import-and-distribute")
    public ResponseEntity<DistributeResponseDTO> ImportLeadsAndDistribute(
            @RequestBody ImportLeadDistributeDTO request,
            @AuthenticationPrincipal UserDetails userdetails
    ){
        return this.comercialeService.ImportLeadsAndDistribute(request, userdetails.getUsername());
    }

    @GetMapping("/customers/all")
    public List<CustomerResponseDTO> GetAllCompanyCustomers(@AuthenticationPrincipal UserDetails userdetails){
        return this.comercialeService.getAllCompanyCustomers(userdetails.getUsername());
    }

    @PostMapping("/distribute/customers")
    public ResponseEntity<DistributeResponseDTO> DistributeCustomers(
            @AuthenticationPrincipal UserDetails userdetails
    ){
        return this.comercialeService.DistributeCustomers(userdetails.getUsername());
    }

    @PostMapping("/distribute/opportunities")
    public ResponseEntity<DistributeResponseDTO> DistributeOpportunities(
            @AuthenticationPrincipal UserDetails userdetails
    ){
        return this.comercialeService.DistributeOpportunities(userdetails.getUsername());
    }

    @PostMapping("/customer/create")
    public CustomerResponseDTO CreateCustomer(
            @RequestBody CustomerRequest request,
            @AuthenticationPrincipal UserDetails userdetails
    ){
        return this.comercialeService.createCustomer(request, userdetails.getUsername());
    }

    @PostMapping("/customer/import/csv")
    public ImportResultDTO ImportCustomersCsv(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userdetails
    ){
        return this.comercialeService.importCustomersFromCsv(userdetails.getUsername(), file);
    }

    @GetMapping(value = "/customer/export/csv", produces = "text/csv")
    public ResponseEntity<byte[]> ExportCustomersCsv(
            @AuthenticationPrincipal UserDetails userdetails
    ){
        return this.comercialeService.exportCustomersToCsv(userdetails.getUsername());
    }
}
