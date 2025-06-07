package com.levanz.customer.config;

import com.levanz.customer.entity.Admin;
import com.levanz.customer.entity.Role;
import com.levanz.customer.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner resetAndCreateAdmin() {
        return args -> {
            String defaultUsername = "admin";
            String defaultPassword = "admin123";

            adminRepository.findByUsername(defaultUsername)
                .ifPresent(existing -> {
                    adminRepository.delete(existing);
                    System.out.println("🧹 Removed existing admin user.");
                });

            Admin newAdmin = new Admin();
            newAdmin.setUsername(defaultUsername);
            newAdmin.setPassword(passwordEncoder.encode(defaultPassword));
            newAdmin.setRole(Role.ADMIN);  

            adminRepository.save(newAdmin);
            System.out.println("✅ Fresh admin created: " 
                + defaultUsername + "/" + defaultPassword);
        };
    }
}
