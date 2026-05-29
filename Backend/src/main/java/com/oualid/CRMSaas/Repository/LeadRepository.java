package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Company;
import com.oualid.CRMSaas.Entity.Lead;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LeadRepository extends JpaRepository<Lead , UUID> {
    List<Lead> findAllByAssignedTo(User user);
    Page<Lead> findByAssignedTo(User user, Pageable pageable);

    long countByAssignedTo(User user);
    long countByCompany(Company company);

    @Query("SELECT l.status, COUNT(l) FROM Lead l WHERE l.assignedTo = :user GROUP BY l.status")
    List<Object[]> countByStatusGroupByStatus(@Param("user") User user);

    @Query("SELECT l.status, COUNT(l) FROM Lead l WHERE l.company = :company GROUP BY l.status")
    List<Object[]> countByStatusGroupByCompany(@Param("company") Company company);

    List<Lead> findByCompany(Company company);
}
