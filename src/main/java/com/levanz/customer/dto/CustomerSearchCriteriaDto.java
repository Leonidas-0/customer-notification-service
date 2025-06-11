package com.levanz.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchCriteriaDto {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean optedIn; 
    private String channel; 
}
