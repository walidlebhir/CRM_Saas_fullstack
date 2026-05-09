package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.RoleUser;

public record ChekAuthResponse(
        String firstName ,
        String lastName ,
        String email ,
        boolean isActive ,
        RoleUser role
) {
}
