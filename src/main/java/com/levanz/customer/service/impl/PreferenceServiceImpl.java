package com.levanz.customer.service.impl;

import com.levanz.customer.dto.PreferenceRequestDto;
import com.levanz.customer.dto.PreferenceResponseDto;
import com.levanz.customer.entity.Preference;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.mapper.PreferenceMapper;
import com.levanz.customer.repository.PreferenceRepository;
import com.levanz.customer.service.PreferenceService;
import com.levanz.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository prefRepo;
    private final CustomerRepository custRepo;
    private final PreferenceMapper mapper;

    public PreferenceServiceImpl(
        PreferenceRepository prefRepo,
        CustomerRepository custRepo,
        PreferenceMapper mapper
    ) {
        this.prefRepo = prefRepo;
        this.custRepo = custRepo;
        this.mapper = mapper;
    }

    @Override
    public PreferenceResponseDto create(Long customerId, PreferenceRequestDto dto) {
        Customer customer = custRepo.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            ));
        Preference entity = mapper.toEntity(dto);
        entity.setCustomer(customer);
        Preference saved = prefRepo.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public List<PreferenceResponseDto> listByCustomer(Long customerId) {
        if (!custRepo.existsById(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            );
        }
        return prefRepo.findByCustomerId(customerId)
                       .stream()
                       .map(mapper::toDto)
                       .collect(Collectors.toList());
    }

    @Override
    public PreferenceResponseDto update(Long customerId, Long prefId, PreferenceRequestDto dto) {
        Customer customer = custRepo.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            ));
        Preference existing = prefRepo.findById(prefId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Preference not found: " + prefId
            ));
        if (!existing.getCustomer().getId().equals(customer.getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Preference does not belong to customer " + customerId
            );
        }
        mapper.updateEntity(dto, existing);
        Preference updated = prefRepo.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long customerId, Long prefId) {
        Preference existing = prefRepo.findById(prefId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Preference not found: " + prefId
            ));
        if (!existing.getCustomer().getId().equals(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Preference does not belong to customer " + customerId
            );
        }
        prefRepo.delete(existing);
    }
}
