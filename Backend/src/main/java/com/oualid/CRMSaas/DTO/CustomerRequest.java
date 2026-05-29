package com.oualid.CRMSaas.DTO;

public record CustomerRequest(
        String name ,
        String email ,
        String phone,
        String website ,
        String adress
) {
}
