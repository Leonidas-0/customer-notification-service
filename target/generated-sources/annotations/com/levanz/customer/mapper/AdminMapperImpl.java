package com.levanz.customer.mapper;

import com.levanz.customer.dto.AdminDto;
import com.levanz.customer.entity.Admin;
import com.levanz.customer.entity.Role;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-09T08:35:32+0400",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public Admin toEntity(AdminDto dto) {
        if ( dto == null ) {
            return null;
        }

        Admin admin = new Admin();

        admin.setPassword( dto.getPassword() );
        if ( dto.getRole() != null ) {
            admin.setRole( Enum.valueOf( Role.class, dto.getRole() ) );
        }
        admin.setUsername( dto.getUsername() );

        return admin;
    }

    @Override
    public AdminDto toDto(Admin entity) {
        if ( entity == null ) {
            return null;
        }

        AdminDto adminDto = new AdminDto();

        adminDto.setPassword( entity.getPassword() );
        if ( entity.getRole() != null ) {
            adminDto.setRole( entity.getRole().name() );
        }
        adminDto.setUsername( entity.getUsername() );

        return adminDto;
    }

    @Override
    public void updateEntity(AdminDto dto, Admin entity) {
        if ( dto == null ) {
            return;
        }

        entity.setPassword( dto.getPassword() );
        if ( dto.getRole() != null ) {
            entity.setRole( Enum.valueOf( Role.class, dto.getRole() ) );
        }
        else {
            entity.setRole( null );
        }
        entity.setUsername( dto.getUsername() );
    }
}
