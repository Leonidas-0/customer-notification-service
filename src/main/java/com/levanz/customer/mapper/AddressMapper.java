package com.levanz.customer.mapper;

import com.levanz.customer.dto.AddressRequestDto;
import com.levanz.customer.dto.AddressResponseDto;
import com.levanz.customer.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    Address toEntity(AddressRequestDto dto);

    AddressResponseDto toDto(Address entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateEntity(AddressRequestDto dto, @MappingTarget Address entity);
}