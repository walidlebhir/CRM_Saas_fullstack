package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Enum.Priority;
import com.oualid.CRMSaas.Entity.Enum.TaskStatus;
import com.oualid.CRMSaas.Entity.Task;
import com.oualid.CRMSaas.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAssignedto(User user, Pageable pageable);

    Page<Task> findByAssignedtoAndStatus(User user, TaskStatus status, Pageable pageable);

    Page<Task> findByAssignedtoAndPriority(User user, Priority priority, Pageable pageable);

    Page<Task> findByAssignedtoAndStatusAndPriority(User user, TaskStatus status, Priority priority, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.assignedto = :user AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Task> findByFilters(@Param("user") User user,
                             @Param("status") TaskStatus status,
                             @Param("priority") Priority priority,
                             @Param("search") String search,
                             Pageable pageable);

    long countByAssignedtoAndStatus(User user, TaskStatus status);

    long countByAssignedtoAndPriority(User user, Priority priority);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedto = :user AND t.dueDate < :today AND t.status <> 'DONE'")
    long countOverdue(@Param("user") User user, @Param("today") LocalDate today);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedto = :user AND t.dueDate BETWEEN :today AND :end AND t.status <> 'DONE'")
    long countUpcoming(@Param("user") User user, @Param("today") LocalDate today, @Param("end") LocalDate end);

    @Query("SELECT t FROM Task t WHERE t.assignedto = :user AND t.dueDate < :today AND t.status <> 'DONE' ORDER BY t.dueDate ASC")
    List<Task> findOverdue(@Param("user") User user, @Param("today") LocalDate today);

    @Query("SELECT t FROM Task t WHERE t.assignedto = :user AND t.dueDate BETWEEN :today AND :end AND t.status <> 'DONE' ORDER BY t.dueDate ASC")
    List<Task> findUpcoming(@Param("user") User user, @Param("today") LocalDate today, @Param("end") LocalDate end);
}