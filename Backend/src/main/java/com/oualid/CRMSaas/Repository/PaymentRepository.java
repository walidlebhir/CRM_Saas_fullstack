package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCompanyIdOrderByCreatedAtDesc(UUID companyId);
    Optional<Payment> findByCompanyIdAndMonthAndYear(UUID companyId, int month, int year);
}
