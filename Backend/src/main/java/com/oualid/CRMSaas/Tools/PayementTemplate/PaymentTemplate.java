package com.oualid.CRMSaas.Tools.PayementTemplate;

import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Repository.PaymentRepository;

public abstract class PaymentTemplate {

    protected PaymentRepository paymentRepository;

    public PaymentTemplate(PaymentRepository repo) {
        this.paymentRepository = repo;
    }

    // TEMPLATE METHOD
    public final Payment processPayment(PayementRequest datapayement) {

        validate(datapayement);

        boolean isCorrectForm = Execute(datapayement);

        Payment newPayment = Payment.builder()
                .amount(datapayement.amout())
                .transactionId(datapayement.transactionId())
                .method(datapayement.method())
                .status(datapayement.status())
                .userId(datapayement.user_id())
                .build();

        return this.paymentRepository.save(newPayment);
    }

    protected abstract void validate(PayementRequest payment);
    protected abstract boolean Execute(PayementRequest datarequest);

    // ajouter notification
    protected void notifyClient(Payment payment) {
        System.out.println("Notification sent: " + payment.getStatus());
    }
}
