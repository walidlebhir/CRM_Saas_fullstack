package com.oualid.CRMSaas.Web.APi.WebHooks;


import com.oualid.CRMSaas.Services.WebHooksService.WebHooksLeadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class LeadWebHooksRestController {

    // injection service :
    private  final WebHooksLeadService webhoksservice ;
    public LeadWebHooksRestController(WebHooksLeadService webhoksservice ){
        this.webhoksservice = webhoksservice ;
    }

    @PostMapping("/leads/{source}")
    public ResponseEntity<String> WeebHooksLeadSystem(@PathVariable String source ,
                                                      @RequestBody Object pyload ){

        return  this.webhoksservice.ProccesLead(source , pyload);
    }
}
