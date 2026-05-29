package com.oualid.CRMSaas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oualid.CRMSaas.Entity.Enum.Priority;
import com.oualid.CRMSaas.Entity.Enum.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"tasks", "activities", "hibernateLazyInitializer", "handler"})
    private Opportunity opportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"tasks", "leads", "customers", "opportunities", "activities", "hibernateLazyInitializer", "handler"})
    private User assignedto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"users", "leads", "tasks", "hibernateLazyInitializer", "handler"})
    private Company company;

}
