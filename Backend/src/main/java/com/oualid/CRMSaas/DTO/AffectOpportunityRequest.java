package com.oualid.CRMSaas.DTO;

import java.util.UUID;

public record AffectOpportunityRequest(
        UUID id_customer ,
        Long id_opportunity
) {

}
