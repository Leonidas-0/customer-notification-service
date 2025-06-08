package com.levanz.customer.mapper;

import com.levanz.customer.dto.PreferenceRequestDto;
import com.levanz.customer.dto.PreferenceResponseDto;
import com.levanz.customer.entity.Preference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PreferenceMapper {
    Preference toEntity(PreferenceRequestDto dto);

    PreferenceResponseDto toDto(Preference entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateEntity(PreferenceRequestDto dto, @MappingTarget Preference entity);
}