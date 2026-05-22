package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Opportunity;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity , Long> {
    List<Opportunity> findByAssignedTo(User user);
}
