package com.levanz.customer.mapper;

import com.levanz.customer.dto.NotificationRequestDto;
import com.levanz.customer.dto.NotificationResponseDto;
import com.levanz.customer.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel="spring", unmappedTargetPolicy=ReportingPolicy.IGNORE)
public interface NotificationMapper {
    Notification toEntity(NotificationRequestDto dto);

    NotificationResponseDto toDto(Notification entity);

    @Mapping(target="id", ignore=true)
    @Mapping(target="customer", ignore=true)
    void updateEntity(NotificationRequestDto dto, @MappingTarget Notification entity);
}
