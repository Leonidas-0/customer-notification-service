package com.levanz.customer.service;

import com.levanz.customer.entity.Admin;
import java.util.List;

public interface AdminService {
    Admin create(Admin admin);
    Admin update(Long id, Admin admin);
    void delete(Long id);
    Admin getById(Long id);
    List<Admin> getAll();
}
