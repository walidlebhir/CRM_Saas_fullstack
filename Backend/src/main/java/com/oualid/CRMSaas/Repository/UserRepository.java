package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Company;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User , UUID> {
    User findByEmail(String email);
    List<User> findByCompany(Company company);
}
