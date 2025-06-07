package com.levanz.customer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    private String message;

    @Enumerated(EnumType.STRING)
    private DeliveryState state = DeliveryState.PENDING;

    private Instant createdAt = Instant.now();
}
