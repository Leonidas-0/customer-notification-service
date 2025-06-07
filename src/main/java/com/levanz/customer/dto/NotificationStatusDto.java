package com.levanz.customer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationStatusDto {
    private Long            id;
    private String          template;
    private String          deliveryState;   
    private LocalDateTime   sentAt;
}
