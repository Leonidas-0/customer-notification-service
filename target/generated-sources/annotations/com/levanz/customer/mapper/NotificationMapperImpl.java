package com.levanz.customer.mapper;

import com.levanz.customer.dto.NotificationRequestDto;
import com.levanz.customer.dto.NotificationResponseDto;
import com.levanz.customer.entity.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-08T20:27:11+0400",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public Notification toEntity(NotificationRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Notification notification = new Notification();

        notification.setStatus( dto.getStatus() );

        return notification;
    }

    @Override
    public NotificationResponseDto toDto(Notification entity) {
        if ( entity == null ) {
            return null;
        }

        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();

        notificationResponseDto.setCreatedAt( entity.getCreatedAt() );
        notificationResponseDto.setId( entity.getId() );
        notificationResponseDto.setStatus( entity.getStatus() );
        notificationResponseDto.setUpdatedAt( entity.getUpdatedAt() );

        return notificationResponseDto;
    }

    @Override
    public void updateEntity(NotificationRequestDto dto, Notification entity) {
        if ( dto == null ) {
            return;
        }

        entity.setStatus( dto.getStatus() );
    }
}
