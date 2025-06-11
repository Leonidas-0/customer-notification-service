package com.levanz.customer.mapper;

import com.levanz.customer.dto.NotificationRequestDto;
import com.levanz.customer.dto.NotificationResponseDto;
import com.levanz.customer.entity.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T00:30:15+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
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

        notificationResponseDto.setId( entity.getId() );
        notificationResponseDto.setStatus( entity.getStatus() );
        notificationResponseDto.setCreatedAt( entity.getCreatedAt() );
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
