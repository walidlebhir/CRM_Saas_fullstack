package com.oualid.CRMSaas.DTO;

import java.util.UUID;

public record PayementRequest(
        Double amount,
        String method,
        String status,
        String transactionId,
        UUID companyId,
        Integer month,
        Integer year,
        String cardNumber,
        String cardHolder,
        String expiryDate,
        String cvv
) {
}
