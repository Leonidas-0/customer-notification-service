package com.levanz.customer.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String firstName,
        String lastName,
        String primaryEmail,
        LocalDateTime createdAt
) {}
