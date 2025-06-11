package com.levanz.customer.mapper;

import com.levanz.customer.dto.PreferenceRequestDto;
import com.levanz.customer.dto.PreferenceResponseDto;
import com.levanz.customer.entity.Preference;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T00:30:15+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class PreferenceMapperImpl implements PreferenceMapper {

    @Override
    public Preference toEntity(PreferenceRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Preference preference = new Preference();

        preference.setChannel( dto.getChannel() );
        if ( dto.getOptedIn() != null ) {
            preference.setOptedIn( dto.getOptedIn() );
        }

        return preference;
    }

    @Override
    public PreferenceResponseDto toDto(Preference entity) {
        if ( entity == null ) {
            return null;
        }

        PreferenceResponseDto preferenceResponseDto = new PreferenceResponseDto();

        preferenceResponseDto.setId( entity.getId() );
        preferenceResponseDto.setChannel( entity.getChannel() );
        preferenceResponseDto.setOptedIn( entity.isOptedIn() );
        preferenceResponseDto.setUpdatedAt( entity.getUpdatedAt() );

        return preferenceResponseDto;
    }

    @Override
    public void updateEntity(PreferenceRequestDto dto, Preference entity) {
        if ( dto == null ) {
            return;
        }

        entity.setChannel( dto.getChannel() );
        if ( dto.getOptedIn() != null ) {
            entity.setOptedIn( dto.getOptedIn() );
        }
    }
}
