package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private LocalDateTime createdAt ;
    @UpdateTimestamp
    private  LocalDateTime updatedAt ;

    @Enumerated(EnumType.STRING)
    private RoleUser role ;



    @ManyToOne
    private  Company company ;

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Lead> leads ;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Customer> customers ;

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Opportunity> opportunities ;

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Activity> activities ;

    @OneToMany(mappedBy = "assignedto")
    @JsonIgnore
    private List<Task> tasks ;

    // publicite
    @OneToMany(mappedBy = "assignedToo")
    @JsonIgnore
    private List<Publicite> publicites ;


}
