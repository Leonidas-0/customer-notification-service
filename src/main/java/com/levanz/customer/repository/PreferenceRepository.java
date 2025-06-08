package com.levanz.customer.repository;

import com.levanz.customer.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preference,Long> {
    List<Preference> findByCustomerId(Long customerId);
}
