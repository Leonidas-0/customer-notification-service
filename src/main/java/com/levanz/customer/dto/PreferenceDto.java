package com.levanz.customer.dto;

import lombok.Data;

@Data
public class PreferenceDto {
    private Long   id;
    private String channel;   
    private String value;    
}
