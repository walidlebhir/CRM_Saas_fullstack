package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Lead;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeadRepository extends JpaRepository<Lead , UUID> {
    List<Lead> findByAssignedTo(User user);
}
