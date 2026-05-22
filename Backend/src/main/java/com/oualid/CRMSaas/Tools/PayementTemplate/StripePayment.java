package com.oualid.CRMSaas.Tools.PayementTemplate;

import com.oualid.CRMSaas.DTO.PayementRequest;
import com.oualid.CRMSaas.Entity.Payment;
import com.oualid.CRMSaas.Repository.PaymentRepository;

public class StripePayment extends PaymentTemplate{
    public StripePayment(PaymentRepository repo) {
        super(repo);
    }

    @Override
    protected void validate(PayementRequest payment) {

    }

    @Override
    protected boolean Execute(PayementRequest datarequest) {
        System.out.println("Stripe processing...");

        return datarequest.amout() != null && datarequest.amout() > 0;
    }


}
