package com.levanz.customer.specification;

import com.levanz.customer.entity.Customer;
import com.levanz.customer.entity.Preference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class CustomerSpecification {

    public static Specification<Customer> byCriteria(
        String firstName,
        String lastName,
        String email,
        Boolean optedIn
    ) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (StringUtils.hasText(firstName)) {
                p = cb.and(p, cb.like(
                    cb.lower(root.get("firstName")),
                    "%" + firstName.toLowerCase() + "%"
                ));
            }
            if (StringUtils.hasText(lastName)) {
                p = cb.and(p, cb.like(
                    cb.lower(root.get("lastName")),
                    "%" + lastName.toLowerCase() + "%"
                ));
            }
            if (StringUtils.hasText(email)) {
                p = cb.and(p, cb.like(
                    cb.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"
                ));
            }
            if (optedIn != null) {
                Join<Customer, Preference> prefJoin =
                    root.join("preferences", JoinType.LEFT);
                p = cb.and(p, cb.equal(
                    prefJoin.get("optedIn"), optedIn
                ));
                query.distinct(true);
            }
            return p;
        };
    }
}
