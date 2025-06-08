package com.levanz.customer.controller;

import com.levanz.customer.dto.CustomerRequestDto;
import com.levanz.customer.dto.CustomerResponseDto;
import com.levanz.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getOne(@PathVariable Long id) {
        CustomerResponseDto resp = service.one(id);
        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> search(
        @RequestParam(value = "q", required = false, defaultValue = "") String q,
        Pageable pageable
    ) {
        Page<CustomerResponseDto> page = service.search(q, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(
        @Valid @RequestBody CustomerRequestDto dto
    ) {
        CustomerResponseDto created = service.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody CustomerRequestDto dto
    ) {
        CustomerResponseDto updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
