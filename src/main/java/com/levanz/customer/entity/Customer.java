package com.levanz.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // your existing fields:
    private String firstName;
    private String lastName;
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ONE-TO-MANY to Address
    @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true
    )
    private List<Address> addresses = new ArrayList<>();

    // ONE-TO-MANY to Preference
    @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true
    )
    private List<Preference> preferences = new ArrayList<>();

    // ONE-TO-MANY to Notification
    @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true
    )
    private List<Notification> notifications = new ArrayList<>();
}
