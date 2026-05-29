package com.oualid.CRMSaas.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    private Double amount;

    private String method;

    private String status;

    private String transactionId;

    private String cardHolder;

    private String cardLastFour;

    private Integer month;

    private Integer year;

    private UUID companyId;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
