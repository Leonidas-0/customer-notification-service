package com.levanz.customer.mapper;

import com.levanz.customer.dto.AdminDto;
import com.levanz.customer.entity.Admin;
import com.levanz.customer.entity.Role;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-10T01:52:45+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public Admin toEntity(AdminDto dto) {
        if ( dto == null ) {
            return null;
        }

        Admin admin = new Admin();

        admin.setUsername( dto.getUsername() );
        admin.setPassword( dto.getPassword() );
        if ( dto.getRole() != null ) {
            admin.setRole( Enum.valueOf( Role.class, dto.getRole() ) );
        }

        return admin;
    }

    @Override
    public AdminDto toDto(Admin entity) {
        if ( entity == null ) {
            return null;
        }

        AdminDto adminDto = new AdminDto();

        adminDto.setUsername( entity.getUsername() );
        adminDto.setPassword( entity.getPassword() );
        if ( entity.getRole() != null ) {
            adminDto.setRole( entity.getRole().name() );
        }

        return adminDto;
    }

    @Override
    public void updateEntity(AdminDto dto, Admin entity) {
        if ( dto == null ) {
            return;
        }

        entity.setUsername( dto.getUsername() );
        entity.setPassword( dto.getPassword() );
        if ( dto.getRole() != null ) {
            entity.setRole( Enum.valueOf( Role.class, dto.getRole() ) );
        }
        else {
            entity.setRole( null );
        }
    }
}
