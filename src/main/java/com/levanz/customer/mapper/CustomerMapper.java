package com.levanz.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import com.levanz.customer.dto.CustomerRequestDto;
import com.levanz.customer.dto.CustomerResponseDto;
import com.levanz.customer.entity.Customer;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {
    Customer toEntity(CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(CustomerRequestDto dto, @MappingTarget Customer entity);
}
