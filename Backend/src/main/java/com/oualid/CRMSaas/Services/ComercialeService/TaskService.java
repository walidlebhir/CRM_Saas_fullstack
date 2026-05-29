package com.oualid.CRMSaas.Services.ComercialeService;

import com.oualid.CRMSaas.DTO.*;
import com.oualid.CRMSaas.Entity.Enum.Priority;
import com.oualid.CRMSaas.Entity.Enum.TaskStatus;
import com.oualid.CRMSaas.Entity.Opportunity;
import com.oualid.CRMSaas.Entity.Task;
import com.oualid.CRMSaas.Entity.User;
import com.oualid.CRMSaas.Exception.ResourceNotFoundException;
import com.oualid.CRMSaas.Exception.UserNotFoundException;
import com.oualid.CRMSaas.Repository.OpportunityRepository;
import com.oualid.CRMSaas.Repository.TaskRepository;
import com.oualid.CRMSaas.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class TaskService {

    private final TaskRepository taskrepo;
    private final UserRepository userrepo;
    private final OpportunityRepository opportunityrepo;

    public TaskService(TaskRepository taskrepo, UserRepository userrepo, OpportunityRepository opportunityrepository) {
        this.taskrepo = taskrepo;
        this.userrepo = userrepo;
        this.opportunityrepo = opportunityrepository;
    }

    public List<TaskStatus> getAllTaskStatus() {
        return List.of(TaskStatus.values());
    }

    public List<Priority> getPriority() {
        return List.of(Priority.values());
    }

    public Task createTask(String username, TaskDTO data) {
        User user = userrepo.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new UserNotFoundException("Utilisateur non trouvé : " + username);
        }

        Opportunity opportunity = null;
        if (data.id_opportunity() != null) {
            opportunity = opportunityrepo.findById(data.id_opportunity())
                    .orElseThrow(() -> {
                        log.error("Opportunité introuvable avec l'id : {}", data.id_opportunity());
                        return new ResourceNotFoundException("Opportunité introuvable avec l'id : " + data.id_opportunity());
                    });
        }

        Task task = Task.builder()
                .title(data.title())
                .description(data.description())
                .dueDate(data.dueDate())
                .status(data.status())
                .priority(data.priority())
                .assignedto(user)
                .company(user.getCompany())
                .opportunity(opportunity)
                .build();

        Task saved = taskrepo.save(task);
        log.info("Tâche créée avec succès : id={}, titre={}", saved.getId(), saved.getTitle());
        return saved;
    }

    public Page<Task> getAllTasks(String username, int page, int size, TaskStatus status, Priority priority, String search) {
        User user = userrepo.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new UserNotFoundException("Utilisateur non trouvé : " + username);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").descending().and(Sort.by("createdAt").descending()));

        if (search != null && !search.isBlank()) {
            return taskrepo.findByFilters(user, status, priority, search, pageable);
        }
        if (status != null && priority != null) {
            return taskrepo.findByAssignedtoAndStatusAndPriority(user, status, priority, pageable);
        }
        if (status != null) {
            return taskrepo.findByAssignedtoAndStatus(user, status, pageable);
        }
        if (priority != null) {
            return taskrepo.findByAssignedtoAndPriority(user, priority, pageable);
        }
        return taskrepo.findByAssignedto(user, pageable);
    }

    public Task getTask(Long id) {
        return taskrepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Tâche introuvable avec l'id : {}", id);
                    return new ResourceNotFoundException("Tâche introuvable avec l'id : " + id);
                });
    }

    public Task updateTask(Long id, TaskDTO data) {
        Task task = taskrepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Tâche introuvable avec l'id : {}", id);
                    return new ResourceNotFoundException("Tâche introuvable avec l'id : " + id);
                });

        Opportunity opportunity = task.getOpportunity();
        if (data.id_opportunity() != null) {
            opportunity = opportunityrepo.findById(data.id_opportunity())
                    .orElseThrow(() -> {
                        log.error("Opportunité introuvable avec l'id : {}", data.id_opportunity());
                        return new ResourceNotFoundException("Opportunité introuvable avec l'id : " + data.id_opportunity());
                    });
        }

        task.setTitle(data.title());
        task.setDescription(data.description());
        task.setDueDate(data.dueDate());
        task.setStatus(data.status());
        task.setPriority(data.priority());
        task.setOpportunity(opportunity);

        Task saved = taskrepo.save(task);
        log.info("Tâche mise à jour avec succès : id={}", saved.getId());
        return saved;
    }

    public void deleteTask(Long id) {
        if (!taskrepo.existsById(id)) {
            log.error("Tâche introuvable avec l'id : {}", id);
            throw new ResourceNotFoundException("Tâche introuvable avec l'id : " + id);
        }
        taskrepo.deleteById(id);
        log.info("Tâche supprimée avec succès : id={}", id);
    }

    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskrepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Tâche introuvable avec l'id : {}", id);
                    return new ResourceNotFoundException("Tâche introuvable avec l'id : " + id);
                });
        task.setStatus(status);
        Task saved = taskrepo.save(task);
        log.info("Statut de la tâche {} mis à jour : {}", id, status);
        return saved;
    }

    public Task updateTaskOpportunity(Long id, Long opportunityId) {
        Task task = taskrepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Tâche introuvable avec l'id : {}", id);
                    return new ResourceNotFoundException("Tâche introuvable avec l'id : " + id);
                });
        Opportunity opportunity = opportunityrepo.findById(opportunityId)
                .orElseThrow(() -> {
                    log.error("Opportunité introuvable avec l'id : {}", opportunityId);
                    return new ResourceNotFoundException("Opportunité introuvable avec l'id : " + opportunityId);
                });
        task.setOpportunity(opportunity);
        Task saved = taskrepo.save(task);
        log.info("Opportunité de la tâche {} mise à jour : {}", id, opportunityId);
        return saved;
    }

    public Task reassignTask(Long id, String newUserEmail) {
        Task task = taskrepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Tâche introuvable avec l'id : {}", id);
                    return new ResourceNotFoundException("Tâche introuvable avec l'id : " + id);
                });
        User newUser = userrepo.findByEmail(newUserEmail);
        if (newUser == null) {
            log.error("Utilisateur non trouvé : {}", newUserEmail);
            throw new UserNotFoundException("Utilisateur non trouvé : " + newUserEmail);
        }
        task.setAssignedto(newUser);
        Task saved = taskrepo.save(task);
        log.info("Tâche {} réassignée à {}", id, newUserEmail);
        return saved;
    }

    public TaskStatsResponse getTaskStats(String username) {
        User user = userrepo.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new UserNotFoundException("Utilisateur non trouvé : " + username);
        }

        long total = taskrepo.findByAssignedto(user, Pageable.unpaged()).getTotalElements();
        Map<String, Long> byStatus = new HashMap<>();
        for (TaskStatus s : TaskStatus.values()) {
            long count = taskrepo.countByAssignedtoAndStatus(user, s);
            if (count > 0) byStatus.put(s.name(), count);
        }
        Map<String, Long> byPriority = new HashMap<>();
        for (Priority p : Priority.values()) {
            long count = taskrepo.countByAssignedtoAndPriority(user, p);
            if (count > 0) byPriority.put(p.name(), count);
        }

        LocalDate today = LocalDate.now();
        long overdue = taskrepo.countOverdue(user, today);
        long upcoming = taskrepo.countUpcoming(user, today, today.plusDays(7));

        log.info("Statistiques des tâches pour {}", username);
        return new TaskStatsResponse(total, byStatus, byPriority, overdue, upcoming);
    }

    public List<Task> getUpcomingTasks(String username, int days) {
        User user = userrepo.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new UserNotFoundException("Utilisateur non trouvé : " + username);
        }
        LocalDate today = LocalDate.now();
        return taskrepo.findUpcoming(user, today, today.plusDays(days));
    }

    public List<Task> getOverdueTasks(String username) {
        User user = userrepo.findByEmail(username);
        if (user == null) {
            log.error("Utilisateur non trouvé : {}", username);
            throw new UserNotFoundException("Utilisateur non trouvé : " + username);
        }
        return taskrepo.findOverdue(user, LocalDate.now());
    }

    private long countByAssignedto(User user) {
        return taskrepo.findByAssignedto(user, Pageable.unpaged()).getTotalElements();
    }
}