package com.oualid.CRMSaas.DTO;

import java.util.UUID;

public record PayementRequest(
        Double amout ,
        String method ,
        String status,
        String transactionId ,
        UUID user_id
) {
}
