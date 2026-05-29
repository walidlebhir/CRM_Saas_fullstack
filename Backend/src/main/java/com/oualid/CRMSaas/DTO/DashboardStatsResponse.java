package com.oualid.CRMSaas.DTO;

import java.util.List;
import java.util.Map;

public record DashboardStatsResponse(
    long totalLeads,
    long totalOpportunities,
    long totalCustomers,
    Map<String, Long> leadsByStatus,
    Map<String, Long> opportunitiesByStage,
    List<EvolutionPoint> evolution
) {}
