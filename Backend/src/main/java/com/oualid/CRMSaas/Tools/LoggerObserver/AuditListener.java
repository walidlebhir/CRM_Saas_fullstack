package com.oualid.CRMSaas.Tools.LoggerObserver;


import com.oualid.CRMSaas.Entity.AuditLogger;
import com.oualid.CRMSaas.Repository.AuditLoggerRepository;
import org.springframework.stereotype.Component;

@Component
public class AuditListener  implements EventListener {

    private  final AuditLoggerRepository auditloggerrepository ;

    public AuditListener(AuditLoggerRepository auditlogRepo ){
        this.auditloggerrepository = auditlogRepo ;
    }

    @Override
    public void handel(AuditLogger auditlogger) {

        // Ajouter logger dans repo
        try{
            AuditLogger newauditlogger  = AuditLogger.builder()
                    .type(auditlogger.getType())
                    .log(auditlogger.getLog())
                    .build();

            this.auditloggerrepository.save(newauditlogger) ;
        }catch(Exception ex){
            // debug
            System.out.println("[EXCEPTION] : "+ ex.getMessage()) ;
            throw  new RuntimeException("problemme dans le service ");
        }




    }
}
