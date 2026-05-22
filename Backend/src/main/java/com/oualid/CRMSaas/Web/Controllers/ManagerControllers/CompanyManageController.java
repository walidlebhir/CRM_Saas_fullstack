package com.oualid.CRMSaas.Web.Controllers.ManagerControllers;

import com.oualid.CRMSaas.DTO.CompanyRequestDTO;
import com.oualid.CRMSaas.Entity.Company;
import com.oualid.CRMSaas.Services.ManagerService.CompanyManageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/manager")
public class CompanyManageController {

    private final CompanyManageService companyservice ;

    public CompanyManageController(CompanyManageService companyservice ){
        this.companyservice = companyservice ;
    }

    // create New company
    @PostMapping("/create/company/")
    public Company CreateNewCompany(@RequestBody CompanyRequestDTO datarequest, @AuthenticationPrincipal UserDetails manager){
        return  this.companyservice.CreateNewCompany(datarequest , manager.getUsername());
    }

    @PutMapping("/update/company/{id}")
    public  Company UpdateInfoCompany(@RequestBody CompanyRequestDTO dataRequestupdate , @PathVariable UUID id ){
        return  this.companyservice.updateDataCompany(dataRequestupdate , id);
    }


    @GetMapping("/show/company/")
    public Company showCompany(@AuthenticationPrincipal UserDetails manager_conect){
        return  this.companyservice.ShowCompany(manager_conect.getUsername());

    }

    @GetMapping("/chek/company")
    public Boolean CHekCompanyExiste(@AuthenticationPrincipal UserDetails userdetail){
        return  this.companyservice.ChekEntrepirseCreate(userdetail.getUsername());

    }

    @PostMapping("/Generate/token")
    public String generateTokenCompany(@AuthenticationPrincipal UserDetails user_conected ){
        return  this.companyservice.GenerateTokenCompany(user_conected.getUsername());

    }


}
