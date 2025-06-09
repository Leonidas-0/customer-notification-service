package com.levanz.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreferenceStatsDto {
    private String channel;
    private long count;
}
