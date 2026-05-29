package com.oualid.CRMSaas.Tools.PayementTemplate;

import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Repository.PaymentRepository;

public class PaypalPayment extends  PaymentTemplate {
    public PaypalPayment(PaymentRepository repo) {
        super(repo);
    }

    @Override
    protected void validate(PayementRequest payment) {

    }

    @Override
    protected boolean Execute(PayementRequest datarequest) {
        System.out.println("Paypal processing...");

        return datarequest.amount() != null && datarequest.amount() > 0;
    }

}
