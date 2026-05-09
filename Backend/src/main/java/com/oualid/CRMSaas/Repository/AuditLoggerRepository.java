package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.AuditLogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLoggerRepository extends JpaRepository<AuditLogger , Long> {
}
