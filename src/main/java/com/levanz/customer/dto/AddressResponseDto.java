package com.levanz.customer.dto;

import com.levanz.customer.entity.AddressType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddressResponseDto {
    private Long id;
    private AddressType type;
    private String country;
    private String city;
    private String street;
    private String zip;
    private LocalDateTime createdAt;
}
