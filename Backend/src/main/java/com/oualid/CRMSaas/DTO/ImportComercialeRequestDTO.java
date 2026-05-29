package com.oualid.CRMSaas.DTO;

import java.util.List;

public record ImportComercialeRequestDTO(
    List<ComercialeRequestDTO> commercials,
    boolean sendEmail
) {}
