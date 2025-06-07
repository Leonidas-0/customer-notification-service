package com.levanz.customer.controller;

import com.levanz.customer.dto.CustomerDto;
import com.levanz.customer.service.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerServiceImpl service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto create(@Valid @RequestBody CustomerDto dto) {
        return service.create(dto);
    }


    @GetMapping
    public Page<CustomerDto> list(Pageable pageable,
                                  @RequestParam(defaultValue = "") String q) {
        return service.search(q, pageable);
    }

    @GetMapping("/{id}")
    public CustomerDto one(@PathVariable Long id) {
        return service.one(id);
    }


    @PutMapping("/{id}")
    public CustomerDto update(@PathVariable Long id,
                              @Valid @RequestBody CustomerDto dto) {
        return service.update(id, dto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
