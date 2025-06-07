package com.levanz.customer.repository;

import com.levanz.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository
        extends JpaRepository<Customer, Long>,
                JpaSpecificationExecutor<Customer> {

    boolean existsByPrimaryEmail(String primaryEmail);

    Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName,
            Pageable pageable
    );
}
