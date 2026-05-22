package com.oualid.CRMSaas.DTO;

import com.oualid.CRMSaas.Entity.Enum.RoleUser;

import java.util.UUID;

public record UserResponseDTO(
        UUID id ,
        String firstName,
        String lastName ,
        String email ,
        boolean isAtive ,
        RoleUser role
) {
}
