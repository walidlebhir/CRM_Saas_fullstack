package com.oualid.CRMSaas.DTO;

import java.util.Map;

public record TaskStatsResponse(
        long total,
        Map<String, Long> byStatus,
        Map<String, Long> byPriority,
        long overdue,
        long upcoming
) {
}
