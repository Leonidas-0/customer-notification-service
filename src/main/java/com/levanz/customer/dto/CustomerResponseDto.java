package com.levanz.customer.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CustomerResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;

    private List<AddressResponseDto> addresses;
    private List<PreferenceResponseDto> preferences;
    private List<NotificationResponseDto> notifications;
}
