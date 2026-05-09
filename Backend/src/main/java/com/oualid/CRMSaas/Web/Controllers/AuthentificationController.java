package com.oualid.CRMSaas.Web.Controllers;


import com.oualid.CRMSaas.DTO.ChekAuthResponse;
import com.oualid.CRMSaas.DTO.LoginDTO;
import com.oualid.CRMSaas.DTO.RegisterDTO;
import com.oualid.CRMSaas.Services.AuthentificationService.AuthService;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authentication")
public class AuthentificationController {
    private AuthService authservice ;
    public  AuthentificationController(AuthService authservice ){
        this.authservice = authservice ;
    }

    // creation Register et Login

    @PostMapping("/register")
    public  String Register(@RequestBody  RegisterDTO registerDatauser){
        return  this.authservice.register(registerDatauser);
    }

    @PostMapping("/login")
    public  String Login(@RequestBody LoginDTO loginDatauser){
        return  this.authservice.login(loginDatauser);
    }

    @GetMapping("/chek/auth")
    public ChekAuthResponse getProfilUserauth(@AuthenticationPrincipal UserDetails userdetail){
        return  this.authservice.chekAuthentification(userdetail.getUsername());
    }
}
