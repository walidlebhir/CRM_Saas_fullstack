package com.oualid.CRMSaas.Web.APi;

import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Repository.UserRepository;
import com.oualid.CRMSaas.Services.Payement.PayementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payement/system")
public class PayementRestController {

    private final PayementService payementservice;
    private final UserRepository userRepository;

    public PayementRestController(PayementService payementservice, UserRepository userRepository) {
        this.payementservice = payementservice;
        this.userRepository = userRepository;
    }

    @PostMapping("/payement/service/{methode}")
    public Payment ExecutePayement(@RequestBody PayementRequest datapayement,
                                   @PathVariable String methode) {
        return this.payementservice.pay(methode, datapayement);
    }

    @GetMapping("/company/payments")
    public ResponseEntity<List<Payment>> getCompanyPayments(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null || user.getCompany() == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Payment> payments = payementservice.getPaymentsByCompany(user.getCompany().getId());
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/company/pay/monthly")
    public ResponseEntity<?> makeMonthlyPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "card") String method,
            @RequestParam(defaultValue = "99.99") Double amount,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String cardHolder,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String cvv
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null || user.getCompany() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Company not found"));
        }
        try {
            Payment payment = payementservice.makeMonthlyPayment(
                    user.getCompany().getId(), month, year, method, amount,
                    cardNumber, cardHolder, expiryDate, cvv
            );
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/company/pay/status")
    public ResponseEntity<?> getMonthlyStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int month,
            @RequestParam int year
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null || user.getCompany() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Company not found"));
        }
        Optional<Payment> payment = payementservice.getMonthlyPaymentStatus(
                user.getCompany().getId(), month, year
        );
        return ResponseEntity.ok(Map.of(
                "paid", payment.isPresent(),
                "payment", payment.orElse(null)
        ));
    }
}
