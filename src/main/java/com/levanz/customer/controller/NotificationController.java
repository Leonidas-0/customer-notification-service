package com.levanz.customer.controller;

import com.levanz.customer.dto.NotificationRequestDto;
import com.levanz.customer.dto.NotificationResponseDto;
import com.levanz.customer.entity.NotificationStatus;
import com.levanz.customer.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDto> create(
        @PathVariable Long customerId,
        @Valid @RequestBody NotificationRequestDto dto
    ) {
        NotificationResponseDto created = service.create(customerId, dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<NotificationResponseDto> listByCustomer(
        @PathVariable Long customerId
    ) {
        return service.listByCustomer(customerId);
    }

    @GetMapping("/status/{status}")
    public List<NotificationResponseDto> listByStatus(
        @PathVariable NotificationStatus status
    ) {
        return service.listByStatus(status);
    }

    @PutMapping("/{notifId}")
    public NotificationResponseDto update(
        @PathVariable Long customerId,
        @PathVariable Long notifId,
        @Valid @RequestBody NotificationRequestDto dto
    ) {
        return service.update(customerId, notifId, dto);
    }

    @DeleteMapping("/{notifId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long customerId,
        @PathVariable Long notifId
    ) {
        service.delete(customerId, notifId);
        return ResponseEntity.noContent().build();
    }
}
