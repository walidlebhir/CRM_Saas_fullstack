package com.oualid.CRMSaas.Tools.LoggerObserver;

import com.oualid.CRMSaas.Entity.AuditLogger;

public interface EventListener {

    void handel(AuditLogger auditlogger ) ;
}
