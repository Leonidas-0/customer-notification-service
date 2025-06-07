package com.levanz.customer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String primaryEmail;
    private LocalDateTime createdAt;
}
