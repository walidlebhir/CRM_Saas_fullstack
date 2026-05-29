package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oualid.CRMSaas.Entity.Enum.OpportunityStage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name ="opportunity")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Long id ;

    private  String title ;
    private BigDecimal amount ;

    @Enumerated(EnumType.STRING)
    private OpportunityStage stage ;

    private  Integer probabilirty ;
    private  String description ;

    @CreationTimestamp
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private  LocalDateTime updatedAt ;

    @ManyToOne
    @JsonIgnoreProperties({"opportunities", "hibernateLazyInitializer", "handler"})
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    private User assignedTo;

    @OneToMany(mappedBy = "opportunity")
    @JsonIgnore
    private List<Activity> activities;

    @OneToMany(mappedBy = "opportunity")
    @JsonIgnore
    private List<Task> tasks;


}
