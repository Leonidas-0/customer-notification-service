package com.levanz.customer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "preferences")
@Getter
@Setter
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String channel; 

    @Column(nullable = false)
    private boolean optedIn;

    @Column(nullable = false, updatable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
