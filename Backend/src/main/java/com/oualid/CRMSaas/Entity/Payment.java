package com.oualid.CRMSaas.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "payment")
@Service
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {


    @Id
    @GeneratedValue
    private Long id;

    private Double amount;

    private String method;   // CARD, PAYPAL...

    private String status;   // PENDING, SUCCESS, FAILED

    private String transactionId;

    private UUID userId;


    @CreationTimestamp
    private LocalDateTime createdAt;


}
