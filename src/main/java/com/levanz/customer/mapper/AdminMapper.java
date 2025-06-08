package com.levanz.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import com.levanz.customer.dto.AdminDto;
import com.levanz.customer.entity.Admin;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AdminMapper {
    Admin toEntity(AdminDto dto);
    AdminDto toDto(Admin entity);
    
    @Mapping(target = "id", ignore = true)
    void updateEntity(AdminDto dto, @MappingTarget Admin entity);
}
