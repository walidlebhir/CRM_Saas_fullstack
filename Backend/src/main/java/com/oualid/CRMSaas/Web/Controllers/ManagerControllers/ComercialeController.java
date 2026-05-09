package com.oualid.CRMSaas.Web.Controllers.ManagerControllers;


import com.oualid.CRMSaas.DTO.ComercialeRequestDTO;
import com.oualid.CRMSaas.DTO.UserResponseDTO;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Services.ManagerService.ComercialeMangementService;
import org.apache.catalina.LifecycleState;
import org.eclipse.angus.mail.util.UUDecoderStream;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/show/all/comerciale")
    public List<UserResponseDTO > getAllcomecriales(@AuthenticationPrincipal UserDetails manager_conect){
        return  this.comercialeService.getALlcommerciauCompany(manager_conect.getUsername());

    }

}
