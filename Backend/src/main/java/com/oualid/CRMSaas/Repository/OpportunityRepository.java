package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity , Long> {
}
