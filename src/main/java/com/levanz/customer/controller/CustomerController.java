package com.levanz.customer.controller;

import com.levanz.customer.dto.CustomerRequest;
import com.levanz.customer.dto.CustomerResponse;
import com.levanz.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse create(@Valid @RequestBody CustomerRequest r) {
        return service.create(r);
    }

    @GetMapping
    public Page<CustomerResponse> list(Pageable p,
                                       @RequestParam(defaultValue = "") String q) {
        return service.search(q, p);
    }

    @GetMapping("{id}")
    public CustomerResponse one(@PathVariable Long id) {
        return service.one(id);
    }

    @PutMapping("{id}")
    public CustomerResponse update(@PathVariable Long id,
                                   @Valid @RequestBody CustomerRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
