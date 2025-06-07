package com.levanz.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Channel channel;    
     
    @Column(name = "`value`")
    private String value;        

    private boolean verified = false;

    @ManyToOne
    private Customer customer;
}
