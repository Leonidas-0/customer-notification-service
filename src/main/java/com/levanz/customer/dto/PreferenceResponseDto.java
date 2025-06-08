package com.levanz.customer.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PreferenceResponseDto {
    private Long id;
    private String channel;
    private boolean optedIn;
    private LocalDateTime updatedAt;
}