package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="customer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id")
    private UUID id ;

    private  String name ;
    private  String email ;
    private  String phone ;
    private String website ;
    private  String addres ;

    @CreationTimestamp
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private LocalDateTime updatedAt ;

    @ManyToOne
    @JsonIgnore
    private  User createdBy ;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Opportunity> opportunities ;



}
