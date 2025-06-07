package com.levanz.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity @Getter @Setter
public class NotificationStatus {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    private DeliveryState status = DeliveryState.PENDING;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    private Customer customer;
}
