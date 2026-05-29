package com.oualid.CRMSaas.Web.Controllers.comercialeController;

import com.oualid.CRMSaas.DTO.*;
import com.oualid.CRMSaas.Entity.Enum.Priority;
import com.oualid.CRMSaas.Entity.Enum.TaskStatus;
import com.oualid.CRMSaas.Entity.Task;
import com.oualid.CRMSaas.Services.ComercialeService.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comerciale/taches")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/enums/status")
    public ResponseEntity<List<TaskStatus>> getTaskStatuses() {
        return ResponseEntity.ok(taskService.getAllTaskStatus());
    }

    @GetMapping("/enums/priority")
    public ResponseEntity<List<Priority>> getPriorities() {
        return ResponseEntity.ok(taskService.getPriority());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TaskDTO data
    ) {
        Task task = taskService.createTask(userDetails.getUsername(), data);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getMyTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String search
    ) {
        Page<Task> tasks = taskService.getAllTasks(
                userDetails.getUsername(), page, size, status, priority, search
        );
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskDTO data
    ) {
        Task task = taskService.updateTask(id, data);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(Map.of("message", "Tâche supprimée avec succès"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusRequest data
    ) {
        Task task = taskService.updateTaskStatus(id, data.status());
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/opportunity")
    public ResponseEntity<Task> updateTaskOpportunity(
            @PathVariable Long id,
            @Valid @RequestBody TaskOpportunityRequest data
    ) {
        Task task = taskService.updateTaskOpportunity(id, data.id_opportunity());
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Task> reassignTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskReassignRequest data
    ) {
        Task task = taskService.reassignTask(id, data.email());
        return ResponseEntity.ok(task);
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStatsResponse> getTaskStats(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(taskService.getTaskStats(userDetails.getUsername()));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Task>> getUpcomingTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(taskService.getUpcomingTasks(userDetails.getUsername(), days));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(taskService.getOverdueTasks(userDetails.getUsername()));
    }
}