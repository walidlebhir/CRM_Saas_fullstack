package com.oualid.CRMSaas.Tools.LoggerObserver;


import com.oualid.CRMSaas.Entity.AuditLogger;
import org.springframework.stereotype.Component;

@Component
public class LoggerListener implements EventListener {


    @Override
    public void handel(AuditLogger auditlogger) {
        System.out.println("[LOG] " + auditlogger.getType() + ":" + auditlogger.getLog());

    }
}
