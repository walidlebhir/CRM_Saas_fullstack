package com.oualid.CRMSaas.Services.Payement;


import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Repository.PaymentRepository;
import com.oualid.CRMSaas.Tools.PayementTemplate.PaymentTemplate;
import com.oualid.CRMSaas.Tools.PayementTemplate.PaypalPayment;
import com.oualid.CRMSaas.Tools.PayementTemplate.StripePayment;
import org.springframework.stereotype.Service;

@Service
public class PayementService {
   private final PaymentRepository payementRepo ;
   public PayementService(PaymentRepository paymentRepo  ){
       this.payementRepo = paymentRepo ;

   }

    public Payment pay(String method, PayementRequest dataPayament ) {

        PaymentTemplate processor;

        switch (method) {
            case "stripe":
                processor = new StripePayment(this.payementRepo);
                break;
            case "paypal":
                processor = new PaypalPayment(this.payementRepo);
                break;
            default:
                throw new RuntimeException("Unknown method");
        }

        return processor.processPayment(dataPayament);
    }

}
