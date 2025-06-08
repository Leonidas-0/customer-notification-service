package com.levanz.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceRequestDto {
    @NotBlank
    private String channel;

    @NotNull
    private Boolean optedIn;
}