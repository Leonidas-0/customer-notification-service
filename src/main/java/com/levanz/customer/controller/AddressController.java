package com.levanz.customer.controller;

import com.levanz.customer.dto.AddressRequestDto;
import com.levanz.customer.dto.AddressResponseDto;
import com.levanz.customer.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AddressResponseDto> create(
        @PathVariable Long customerId,
        @Valid @RequestBody AddressRequestDto dto
    ) {
        AddressResponseDto created = service.create(customerId, dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<AddressResponseDto> list(
        @PathVariable Long customerId
    ) {
        return service.listByCustomer(customerId);
    }

    @PutMapping("/{addressId}")
    public AddressResponseDto update(
        @PathVariable Long customerId,
        @PathVariable Long addressId,
        @Valid @RequestBody AddressRequestDto dto
    ) {
        return service.update(customerId, addressId, dto);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(
        @PathVariable Long customerId,
        @PathVariable Long addressId
    ) {
        service.delete(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}
