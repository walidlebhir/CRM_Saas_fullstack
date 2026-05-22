package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="company")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    private UUID id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "industrie")
    private  String industrie ;

    @Column(name = "email")
    private  String email ;

    @Column(name = "phone")
    private String phone ;
    @Column(name = "address")
    private  String address ;

    @Column(name = "webSite")
    private  String webSite ;

    @Column(name = "tokenwebhooks")
    private String tokenWebHooks;


    @CreationTimestamp
    @Column(name = "createdat")
    private LocalDateTime CreatedAt ;

    @UpdateTimestamp
    @Column(name = "updatedat")
    private  LocalDateTime UpdatedAt ;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<User> users ;

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    private List<Lead> leads ;

    @OneToMany(mappedBy = "company")
    private  List<Task> tasks ;


}
