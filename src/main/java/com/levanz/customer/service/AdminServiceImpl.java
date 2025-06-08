package com.levanz.customer.service;

import com.levanz.customer.dto.AdminDto;
import com.levanz.customer.entity.Admin;
import com.levanz.customer.entity.Role;
import com.levanz.customer.mapper.AdminMapper;
import com.levanz.customer.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository repo;
    private final PasswordEncoder encoder;
    private final AdminMapper mapper;

    public AdminServiceImpl(
        AdminRepository repo,
        PasswordEncoder encoder,
        AdminMapper mapper
    ) {
        this.repo = repo;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public AdminDto create(AdminDto dto) {
        Admin admin = mapper.toEntity(dto);
        admin.setPassword(encoder.encode(dto.getPassword()));
        admin.setRole(Role.ADMIN);
        Admin saved = repo.save(admin);
        return mapper.toDto(saved);
    }

    @Override
    public AdminDto update(Long id, AdminDto dto) {
        Admin existing = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Admin not found: " + id
            ));
        mapper.updateEntity(dto, existing);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPassword(encoder.encode(dto.getPassword()));
        }
        Admin updated = repo.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "Admin not found: " + id
            );
        }
        repo.deleteById(id);
    }

    @Override
    public AdminDto getById(Long id) {
        Admin admin = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Admin not found: " + id
            ));
        return mapper.toDto(admin);
    }

    @Override
    public List<AdminDto> getAll() {
        return repo.findAll().stream()
                   .map(mapper::toDto)
                   .collect(Collectors.toList());
    }
}
