package com.oualid.CRMSaas.Web.Controllers.comercialeController;


import com.oualid.CRMSaas.DTO.EnumStatusLeadResponse;
import com.oualid.CRMSaas.DTO.LeadDTO;
import com.oualid.CRMSaas.DTO.StatusRequest;
import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.Enum.LeadSource;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;
import com.oualid.CRMSaas.Entity.Lead;
import com.oualid.CRMSaas.Services.ComercialeService.LeadsManagementService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/change/status/{id_lead}")
    public ResponseEntity<String> ChangeStatusLead(@PathVariable UUID id_lead , @RequestBody StatusRequest statusRequest ){
        return  this.leadmanageservice.ChangeStatusLead(id_lead , statusRequest);

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

    @GetMapping("/get/all/page")
    public Page<Lead> getLeadsPaginated(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return leadmanageservice.getLeadsPaginated(userDetails.getUsername(), page, size);
    }

    @GetMapping("/show/lead/{id}")
    public Lead showLead(@PathVariable UUID id){
        return  this.leadmanageservice.ShowLead(id);
    }

    @GetMapping("/get/all/source/lead")
    public List<LeadSource> getAllsourceLead(){
        return  this.leadmanageservice.getAllLeadSource();
    }


    // modefication Lead data :

    @PatchMapping("/update/lead/{id_lead}")
    public Lead updateLead(
            @PathVariable UUID id_lead ,
            @RequestBody LeadDTO datalead
    ){
        return  this.leadmanageservice.UpdateLead(id_lead , datalead );
    }

    @GetMapping("/get/status/lead")
    public List<LeadStatus> getLeadStatus(){
        return  this.leadmanageservice.getAllLeadStatus() ;
    }

    @PostMapping(value = "/import/lead/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Lead> importLeadsCsv(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        return leadmanageservice.importLeadsFromCsv(userDetails.getUsername(), file);
    }

}
