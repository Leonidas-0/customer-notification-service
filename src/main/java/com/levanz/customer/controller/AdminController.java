package com.levanz.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.levanz.customer.dto.AdminDto;
import com.levanz.customer.entity.Admin;
import com.levanz.customer.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Admin> create(@Validated @RequestBody AdminDto dto) {
        Admin toCreate = new Admin(dto.getUsername(), dto.getPassword(), dto.getRole());
        return ResponseEntity.ok(service.create(toCreate));
    }

    @GetMapping
    public ResponseEntity<List<Admin>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> update(
            @PathVariable Long id,
            @Validated @RequestBody AdminDto dto) {
        Admin toUpdate = new Admin(dto.getUsername(), dto.getPassword(), dto.getRole());
        return ResponseEntity.ok(service.update(id, toUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
