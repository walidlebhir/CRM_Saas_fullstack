package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Customer;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CustomerRepository extends JpaRepository<Customer , UUID> {
    List<Customer> findByCreatedBy(User user);
}
