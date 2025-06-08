package com.levanz.customer.service.impl;

import com.levanz.customer.dto.NotificationRequestDto;
import com.levanz.customer.dto.NotificationResponseDto;
import com.levanz.customer.entity.Notification;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.entity.NotificationStatus;
import com.levanz.customer.mapper.NotificationMapper;
import com.levanz.customer.repository.NotificationRepository;
import com.levanz.customer.service.NotificationService;
import com.levanz.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notifRepo;
    private final CustomerRepository custRepo;
    private final NotificationMapper mapper;

    public NotificationServiceImpl(
        NotificationRepository notifRepo,
        CustomerRepository custRepo,
        NotificationMapper mapper
    ) {
        this.notifRepo = notifRepo;
        this.custRepo = custRepo;
        this.mapper = mapper;
    }

    @Override
    public NotificationResponseDto create(Long customerId, NotificationRequestDto dto) {
        Customer customer = custRepo.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            ));
        Notification entity = mapper.toEntity(dto);
        entity.setCustomer(customer);
        entity.setUpdatedAt(LocalDateTime.now());
        Notification saved = notifRepo.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public List<NotificationResponseDto> listByCustomer(Long customerId) {
        if (!custRepo.existsById(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            );
        }
        return notifRepo.findByCustomerId(customerId)
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponseDto> listByStatus(NotificationStatus status) {
        return notifRepo.findByStatus(status)
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDto update(Long customerId, Long notifId, NotificationRequestDto dto) {
        Customer customer = custRepo.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            ));
        Notification existing = notifRepo.findById(notifId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Notification not found: " + notifId
            ));
        if (!existing.getCustomer().getId().equals(customer.getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Notification does not belong to customer " + customerId
            );
        }
        mapper.updateEntity(dto, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        Notification updated = notifRepo.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long customerId, Long notifId) {
        Notification existing = notifRepo.findById(notifId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Notification not found: " + notifId
            ));
        if (!existing.getCustomer().getId().equals(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Notification does not belong to customer " + customerId
            );
        }
        notifRepo.delete(existing);
    }
}
