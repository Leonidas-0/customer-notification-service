package com.levanz.customer.repository;

import com.levanz.customer.entity.Notification;
import com.levanz.customer.entity.NotificationStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByCustomerId(Long customerId);
    List<Notification> findByStatus(NotificationStatus status);
}