package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oualid.CRMSaas.Entity.Enum.ActivityType;
import com.oualid.CRMSaas.Entity.Enum.RoleUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    private UUID id ;

    private String firstName ;
    private  String lastName ;

    @Column(unique = true)
    private  String email ;

    private  String password ;
    private  Boolean isActive ;


    @CreationTimestamp
    private LocalDateTime CreatedAt ;
    @UpdateTimestamp
    private  LocalDateTime UpdatedAt ;

    @Enumerated(EnumType.STRING)
    private RoleUser role ;



    @ManyToOne
    private  Company company ;

    @OneToMany(mappedBy = "assignedTo")
    private List<Lead> leads ;

    @OneToMany(mappedBy = "createdBy")
    private List<Customer> customers ;

    @OneToMany(mappedBy = "assignedTo")
    private List<Opportunity> opportunities ;

    @OneToMany(mappedBy = "assignedTo")
    private List<Activity> activities ;




}
