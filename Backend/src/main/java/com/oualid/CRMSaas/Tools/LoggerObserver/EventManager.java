package com.oualid.CRMSaas.Tools.LoggerObserver;


import com.oualid.CRMSaas.Entity.AuditLogger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventManager {

    private  final List<EventListener> listners ;

    public EventManager(List<EventListener> listners){
        this.listners = listners ;
    }

    public  void publish(AuditLogger auditlogger ){
        for(EventListener listner : listners){
            listner.handel(auditlogger);
        }
    }

}
