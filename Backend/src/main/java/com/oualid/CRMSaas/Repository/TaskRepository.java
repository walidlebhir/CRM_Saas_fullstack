package com.oualid.CRMSaas.Repository;

import com.oualid.CRMSaas.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task , Long> {
}
