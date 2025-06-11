package com.levanz.customer.specification;

import com.levanz.customer.entity.Customer;
import com.levanz.customer.entity.Preference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
/**
 * Utility class to build dynamic JPA queries for the Customer entity using the Criteria API.
 * This allows for flexible and type-safe query construction based on provided search criteria.
 */
public class CustomerSpecification {


public static Specification<Customer> byCriteria(
    String firstName,
    String lastName,
    String email,
    Boolean optedIn,
    String channel
) {
    return (root, query, cb) -> {
        Predicate p = cb.conjunction();
        
        Join<Customer, Preference> prefJoin = null; 

        if (StringUtils.hasText(firstName)) {
            p = cb.and(p, cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(lastName)) {
            p = cb.and(p, cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(email)) {
            p = cb.and(p, cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        
        if (StringUtils.hasText(channel)) {
            prefJoin = root.join("preferences", JoinType.LEFT);
            p = cb.and(p, cb.equal(prefJoin.get("channel"), channel));
            query.distinct(true);
        }

        if (optedIn != null) {
            if (prefJoin == null) {
                prefJoin = root.join("preferences", JoinType.LEFT);
            }
            p = cb.and(p, cb.equal(prefJoin.get("optedIn"), optedIn));
            query.distinct(true);
        }
        
        return p;
    };
}
}