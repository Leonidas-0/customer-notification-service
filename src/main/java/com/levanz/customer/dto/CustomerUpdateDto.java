package com.levanz.customer.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateDto {
    @NotNull
    private Long id;

    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
}