package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;

import java.math.BigDecimal;

public record OpportuniteRequest(
        String title,
        BigDecimal amount ,
        OpportunityStage stage ,
        Integer probability ,
        String description

) {
}
