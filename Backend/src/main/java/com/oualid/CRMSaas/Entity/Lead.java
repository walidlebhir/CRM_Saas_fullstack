package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oualid.CRMSaas.Entity.Enum.LeadSource;
import com.oualid.CRMSaas.Entity.Enum.LeadStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "leads")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lead {

    @Id
    @Generated
    @UuidGenerator
    @Column(name = "id")
    private UUID id ;

    private  String firstname ;
    private  String lastname ;
    private  String email ;
    private  String phone ;


    @Enumerated(EnumType.STRING)
    private LeadSource source ;

    @Enumerated(EnumType.STRING)
    private LeadStatus status ;

    @CreationTimestamp
    private LocalDateTime CretaedAt ;

    @UpdateTimestamp
    private  LocalDateTime UpdatedAt ;

    @ManyToOne
    @JsonBackReference
    private  Company company ;

    @ManyToOne
    @JsonIgnore
    private  User assignedTo ;


}
