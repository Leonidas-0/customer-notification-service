package com.levanz.customer.dto;

import com.levanz.customer.entity.NotificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {
    @NotNull
    private NotificationStatus status;
}

