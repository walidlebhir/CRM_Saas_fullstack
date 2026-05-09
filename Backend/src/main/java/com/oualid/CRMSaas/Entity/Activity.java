package com.oualid.CRMSaas.Entity;


import com.oualid.CRMSaas.Entity.Enum.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name ="activity")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Opportunity opportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedTo;

    @OneToMany(mappedBy = "activity")
    private List<Note> notes;
}
