package com.oualid.CRMSaas.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="notes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Note {
    @Id @GeneratedValue
    private Long id;

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Activity activity;

}
