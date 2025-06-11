package com.levanz.customer.controller;

import com.levanz.customer.dto.*;
import com.levanz.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST controller for managing customers.
 * Handles CRUD operations, searching, and batch updates for customers.
 */
@RestController
@RequestMapping("/api/customers")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }
    /**
     * Searches for customers based on a complex set of criteria.
     * @param criteria DTO containing search parameters.
     * @param pageable Pagination and sorting information.
     * @return A paginated list of customers matching the criteria.
     */
    @PostMapping("/search")
    public ResponseEntity<Page<CustomerResponseDto>> searchAdvanced(
        @Valid @RequestBody CustomerSearchCriteriaDto criteria,
        Pageable pageable
    ) {
        return ResponseEntity.ok(service.searchAdvanced(criteria, pageable));
    }
    /**
     * Performs a batch update on multiple customers.
     * @param updates A list of customer update DTOs.
     * @return A list of the updated customers.
     */
    @PutMapping("/batch")
    public ResponseEntity<List<CustomerResponseDto>> batchUpdate(
        @Valid @RequestBody List<CustomerUpdateDto> updates
    ) {
        return ResponseEntity.ok(service.batchUpdate(updates));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.one(id));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> search(
        @RequestParam(value = "q", defaultValue = "") String q,
        Pageable pageable
    ) {
        return ResponseEntity.ok(service.search(q, pageable));
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
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
