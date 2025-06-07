package com.levanz.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class CustomerDto {

    private Long id;

    @NotBlank @Size(max = 60)
    private String firstName;

    @NotBlank @Size(max = 60)
    private String lastName;

    @Email @NotBlank
    private String email;          

    private LocalDateTime createdAt;

    private List<AddressDto>            addresses     = new ArrayList<>();
    private List<PreferenceDto>         preferences   = new ArrayList<>();
    private List<NotificationStatusDto> notifications = new ArrayList<>();
}
