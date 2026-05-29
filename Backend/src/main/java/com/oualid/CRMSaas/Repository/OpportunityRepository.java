package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Opportunity;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity , Long> {
    List<Opportunity> findByAssignedTo(User user);

    long countByAssignedTo(User user);

    @Query("SELECT o.stage, COUNT(o) FROM Opportunity o WHERE o.assignedTo = :user GROUP BY o.stage")
    List<Object[]> countByStageGroupByStage(@Param("user") User user);

    @Query("SELECT YEAR(o.createdAt) as yr, MONTH(o.createdAt) as mt, COUNT(o) as cnt FROM Opportunity o WHERE o.assignedTo = :user GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) ORDER BY yr, mt")
    List<Object[]> countByAssignedToGroupByMonth(@Param("user") User user);

    long countByAssignedToIn(List<User> users);

    @Query("SELECT o.stage, COUNT(o) FROM Opportunity o WHERE o.assignedTo IN :users GROUP BY o.stage")
    List<Object[]> countByStageGroupByUsers(@Param("users") List<User> users);

    @Query("SELECT YEAR(o.createdAt) as yr, MONTH(o.createdAt) as mt, COUNT(o) as cnt FROM Opportunity o WHERE o.assignedTo IN :users GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) ORDER BY yr, mt")
    List<Object[]> countByAssignedToInGroupByMonth(@Param("users") List<User> users);

    List<Opportunity> findByAssignedToIn(List<User> users);
}
