package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oualid.CRMSaas.Entity.Enum.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activity")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Activity {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    private String subject;
    private String description;

    private LocalDateTime activityDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"activities", "notes", "hibernateLazyInitializer", "handler"})
    private Opportunity opportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"tasks", "leads", "customers", "opportunities", "activities", "hibernateLazyInitializer", "handler"})
    private User assignedTo;

    @OneToMany(mappedBy = "activity")
    @JsonIgnore
    private List<Note> notes;
}
