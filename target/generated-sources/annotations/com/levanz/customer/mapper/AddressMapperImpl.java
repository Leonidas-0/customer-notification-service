package com.levanz.customer.mapper;

import com.levanz.customer.dto.AddressRequestDto;
import com.levanz.customer.dto.AddressResponseDto;
import com.levanz.customer.entity.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T02:09:11+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address toEntity(AddressRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Address address = new Address();

        address.setType( dto.getType() );
        address.setCountry( dto.getCountry() );
        address.setCity( dto.getCity() );
        address.setStreet( dto.getStreet() );
        address.setZip( dto.getZip() );

        return address;
    }

    @Override
    public AddressResponseDto toDto(Address entity) {
        if ( entity == null ) {
            return null;
        }

        AddressResponseDto addressResponseDto = new AddressResponseDto();

        addressResponseDto.setId( entity.getId() );
        addressResponseDto.setType( entity.getType() );
        addressResponseDto.setCountry( entity.getCountry() );
        addressResponseDto.setCity( entity.getCity() );
        addressResponseDto.setStreet( entity.getStreet() );
        addressResponseDto.setZip( entity.getZip() );
        addressResponseDto.setCreatedAt( entity.getCreatedAt() );

        return addressResponseDto;
    }

    @Override
    public void updateEntity(AddressRequestDto dto, Address entity) {
        if ( dto == null ) {
            return;
        }

        entity.setType( dto.getType() );
        entity.setCountry( dto.getCountry() );
        entity.setCity( dto.getCity() );
        entity.setStreet( dto.getStreet() );
        entity.setZip( dto.getZip() );
    }
}
