package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment , Long > {
}
