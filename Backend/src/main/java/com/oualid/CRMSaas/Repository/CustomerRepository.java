package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Customer;
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
public interface CustomerRepository extends JpaRepository<Customer , UUID> {
    List<Customer> findAllByCreatedBy(User user);
    Page<Customer> findByCreatedBy(User user, Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.createdBy = :user AND (:search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Customer> findByCreatedByAndSearch(@Param("user") User user, @Param("search") String search, Pageable pageable);

    long countByCreatedBy(User user);

    @Query("SELECT YEAR(c.createdAt) as yr, MONTH(c.createdAt) as mt, COUNT(c) as cnt FROM Customer c WHERE c.createdBy = :user GROUP BY YEAR(c.createdAt), MONTH(c.createdAt) ORDER BY yr, mt")
    List<Object[]> countByCreatedByGroupByMonth(@Param("user") User user);

    long countByCreatedByIn(List<User> users);

    @Query("SELECT YEAR(c.createdAt) as yr, MONTH(c.createdAt) as mt, COUNT(c) as cnt FROM Customer c WHERE c.createdBy IN :users GROUP BY YEAR(c.createdAt), MONTH(c.createdAt) ORDER BY yr, mt")
    List<Object[]> countByCreatedByInGroupByMonth(@Param("users") List<User> users);

    List<Customer> findAllByCreatedByIn(List<User> users);
}
