package com.oualid.CRMSaas.Web.APi;


import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Services.Payement.PayementService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payement/system")
public class PayementRestController {
    // injection service Payemnet :

    private  final PayementService payementservice ;

    public PayementRestController(PayementService payementservice ){
        this.payementservice = payementservice  ;
    }

    @PostMapping("/payement/service/{methode}")
    public Payment Excecutepayement(PayementRequest datapayement ,
                                    @PathVariable String methode ){
        return  this.payementservice.pay(methode , datapayement);
    }
}
