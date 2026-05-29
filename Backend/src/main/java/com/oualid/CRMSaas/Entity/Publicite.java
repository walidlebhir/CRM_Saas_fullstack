package com.oualid.CRMSaas.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "publicite")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Publicite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String titre ;

    @Column(columnDefinition = "TEXT")
    private String contenue ;

    private String imageUrl ;

    @CreationTimestamp
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private LocalDateTime updatedAt ;

    @ManyToOne
    @JsonIgnore
    private User assignedToo;


}
