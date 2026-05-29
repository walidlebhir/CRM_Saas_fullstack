package com.oualid.CRMSaas.Services.Payement;

import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Repository.PaymentRepository;
import com.oualid.CRMSaas.Tools.PayementTemplate.PaymentTemplate;
import com.oualid.CRMSaas.Tools.PayementTemplate.PaypalPayment;
import com.oualid.CRMSaas.Tools.PayementTemplate.StripePayment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayementService {
   private final PaymentRepository payementRepo;

   public PayementService(PaymentRepository paymentRepo) {
       this.payementRepo = paymentRepo;
   }

    public Payment pay(String method, PayementRequest dataPayament) {
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

    public List<Payment> getPaymentsByCompany(UUID companyId) {
        return payementRepo.findByCompanyIdOrderByCreatedAtDesc(companyId);
    }

    public Optional<Payment> getMonthlyPaymentStatus(UUID companyId, int month, int year) {
        return payementRepo.findByCompanyIdAndMonthAndYear(companyId, month, year);
    }

    public Payment makeMonthlyPayment(UUID companyId, int month, int year, String method, Double amount,
                                      String cardNumber, String cardHolder, String expiryDate, String cvv) {
        Optional<Payment> existing = payementRepo.findByCompanyIdAndMonthAndYear(companyId, month, year);
        if (existing.isPresent()) {
            throw new RuntimeException("Payment already exists for this month");
        }
        PayementRequest request = new PayementRequest(
                amount, method, "SUCCESS", "TXN-" + UUID.randomUUID().toString().substring(0, 8),
                companyId, month, year, cardNumber, cardHolder, expiryDate, cvv
        );
        return pay(method, request);
    }
}
