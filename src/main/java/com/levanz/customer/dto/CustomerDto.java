package com.levanz.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *  Single DTO used for both create/update requests and responses.
 *  — If you return it from controllers, Jackson will simply serialise it to JSON.
 */
public record CustomerDto(

        @NotBlank                  @Size(max = 100)
        String firstName,

        @NotBlank                  @Size(max = 100)
        String lastName,

        @NotBlank @Email           @Size(max = 150)
        String email

) {}
