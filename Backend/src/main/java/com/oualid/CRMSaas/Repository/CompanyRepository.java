package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company , UUID> {
}
