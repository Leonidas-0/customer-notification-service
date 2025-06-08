package com.levanz.customer.controller;

import com.levanz.customer.dto.PreferenceRequestDto;
import com.levanz.customer.dto.PreferenceResponseDto;
import com.levanz.customer.service.PreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/preferences")
public class PreferenceController {

    private final PreferenceService service;

    public PreferenceController(PreferenceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PreferenceResponseDto> create(
        @PathVariable Long customerId,
        @Valid @RequestBody PreferenceRequestDto dto
    ) {
        PreferenceResponseDto created = service.create(customerId, dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<PreferenceResponseDto> list(
        @PathVariable Long customerId
    ) {
        return service.listByCustomer(customerId);
    }

    @PutMapping("/{prefId}")
    public PreferenceResponseDto update(
        @PathVariable Long customerId,
        @PathVariable Long prefId,
        @Valid @RequestBody PreferenceRequestDto dto
    ) {
        return service.update(customerId, prefId, dto);
    }

    @DeleteMapping("/{prefId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long customerId,
        @PathVariable Long prefId
    ) {
        service.delete(customerId, prefId);
        return ResponseEntity.noContent().build();
    }
}
