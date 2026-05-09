package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity , Long> {
}
