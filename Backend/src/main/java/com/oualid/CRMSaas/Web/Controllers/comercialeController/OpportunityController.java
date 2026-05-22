package com.oualid.CRMSaas.Web.Controllers.comercialeController;


import com.oualid.CRMSaas.DTO.AffectOpportunityRequest;
import com.oualid.CRMSaas.DTO.OpportuniteRequest;
import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;
import com.oualid.CRMSaas.Entity.Opportunity;
import com.oualid.CRMSaas.Services.ComercialeService.OpportunityService;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comerciale")
public class OpportunityController {

    private  final OpportunityService opportunityservice ;

    public OpportunityController(OpportunityService opportunityservice ){
        this.opportunityservice = opportunityservice;
    }

    @GetMapping("/get/stage/opportunity")
    public List<OpportunityStage> getOpportunityStage(){
        return  this.opportunityservice.getAllStageOpportunite();
    }

    @PostMapping("/create/new/opportunity")
    public Opportunity CreateNewOpportunity(@RequestBody  OpportuniteRequest dataRequestOpportunity , @AuthenticationPrincipal UserDetails user_conected){
        return  this.opportunityservice.CreateNewOpportunite(dataRequestOpportunity  , user_conected.getUsername());
    }

    @GetMapping("/get/all/opportunity")
    public List<Opportunity> getAllOpportunity(@AuthenticationPrincipal UserDetails userdetail ){
        return  this.opportunityservice.ListAllOpportunity(userdetail.getUsername())  ;
    }

    @GetMapping("/show/opportnity/{id}")
    public Opportunity showopportuity(@PathVariable Long id ){
        return  this.opportunityservice.ShowOpportunity(id) ;
    }

    @PostMapping("/affcete/opportunity")
    public ResponseEntity<String> AffectOpportunity(@RequestBody AffectOpportunityRequest dataAffect){
        return  this.opportunityservice.AffceteOpportunityToclinet(dataAffect);
    }
}
