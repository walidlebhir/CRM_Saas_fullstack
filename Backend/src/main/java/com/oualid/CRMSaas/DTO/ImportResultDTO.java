package com.oualid.CRMSaas.DTO;

public record ImportResultDTO(
    int count,
    int errors,
    String message
) {}
