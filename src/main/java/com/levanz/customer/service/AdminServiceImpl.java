package com.levanz.customer.service;

import com.levanz.customer.entity.Admin;
import com.levanz.customer.repository.AdminRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    private final AdminRepository repo;

    public AdminServiceImpl(AdminRepository repo) {
        this.repo = repo;
    }

    @Override
    public Admin create(Admin admin) {
        return repo.save(admin);
    }

    @Override
    public Admin update(Long id, Admin admin) {
        Admin existing = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Admin not found with id " + id
            ));
        existing.setUsername(admin.getUsername());
        existing.setPassword(admin.getPassword());
        existing.setRole(admin.getRole());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        Admin toDelete = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Admin not found with id " + id
            ));
        repo.delete(toDelete);
    }

    @Override
    public Admin getById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Admin not found with id " + id
            ));
    }

    @Override
    public List<Admin> getAll() {
        return repo.findAll();
    }
}
