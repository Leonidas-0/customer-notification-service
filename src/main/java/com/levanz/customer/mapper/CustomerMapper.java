package com.levanz.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.levanz.customer.dto.CustomerDto;
import com.levanz.customer.entity.Customer;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CustomerMapper {


    Customer toEntity(CustomerDto dto);


    CustomerDto toDto(Customer entity);

    void updateEntity(CustomerDto dto, @MappingTarget Customer entity);
}
