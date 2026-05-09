package com.oualid.CRMSaas.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditlogger")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;
    private String type ;
    private String log ;

    @CreationTimestamp
    private LocalDateTime CreatedAt ;

    @UpdateTimestamp
    private  LocalDateTime UpdatedAt ;


}
