package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.RoleUser;

public record UserResponseDTO(
        String firstName,
        String lastName ,
        String email ,
        boolean isAtive ,
        RoleUser role
) {
}
