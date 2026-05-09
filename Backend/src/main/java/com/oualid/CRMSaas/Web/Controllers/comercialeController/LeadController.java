package com.oualid.CRMSaas.Web.Controllers.comercialeController;


import com.oualid.CRMSaas.DTO.EnumStatusLeadResponse;
import com.oualid.CRMSaas.DTO.LeadDTO;
import com.oualid.CRMSaas.DTO.StatusRequest;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;
import com.oualid.CRMSaas.Entity.Lead;
import com.oualid.CRMSaas.Services.ComercialeService.LeadsManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comerciale")
public class LeadController {

    private final LeadsManagementService leadmanageservice ;
    public LeadController(LeadsManagementService leadmanageservice ){
        this.leadmanageservice = leadmanageservice ;
    }

    @GetMapping("/lead-status")
    public List<EnumStatusLeadResponse> getLeadStatuses() {
        return Arrays.stream(LeadStatus.values())
                .map(e -> new EnumStatusLeadResponse(e.name()))
                .toList();
    }

    // create new Lead
    @PostMapping("/create/lead")
    public Lead   CreateNewLead(@RequestBody LeadDTO datalead , @AuthenticationPrincipal UserDetails comerciale_conected ){
        return  this.leadmanageservice.CreatedNewLead(datalead , comerciale_conected.getUsername());

    }

    @PatchMapping("/change/status/{id_lead}")
    public ResponseEntity<String> ChangeStatusLead(@PathVariable UUID id_lead , @RequestBody StatusRequest statusRequest ){
        return  this.ChangeStatusLead(id_lead , statusRequest);

    }

    @DeleteMapping("/delet/lead/{id}")
    public ResponseEntity<String> DeleteLead(@PathVariable UUID id ){
        return  this.leadmanageservice.DeleteLead(id);
    }

    @PostMapping("/transform/lead/{id}")
    public Customer TransformLeadCusomer(@PathVariable UUID id ){
        return  this.leadmanageservice.TransformLeadEncustomer(id);

    }

    @GetMapping("/get/all")
    public  List<Lead> getALlLead(@AuthenticationPrincipal UserDetails userdetails){
        return  this.leadmanageservice.getAllLeads(userdetails.getUsername());

    }

    @GetMapping("/show/lead/{id}")
    public Lead showLead(@PathVariable UUID id){
        return  this.leadmanageservice.ShowLead(id);
    }


}
