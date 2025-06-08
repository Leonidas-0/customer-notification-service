package com.levanz.customer.mapper;

import com.levanz.customer.dto.AddressRequestDto;
import com.levanz.customer.dto.AddressResponseDto;
import com.levanz.customer.entity.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-08T20:27:11+0400",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public Address toEntity(AddressRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Address address = new Address();

        address.setCity( dto.getCity() );
        address.setCountry( dto.getCountry() );
        address.setStreet( dto.getStreet() );
        address.setType( dto.getType() );
        address.setZip( dto.getZip() );

        return address;
    }

    @Override
    public AddressResponseDto toDto(Address entity) {
        if ( entity == null ) {
            return null;
        }

        AddressResponseDto addressResponseDto = new AddressResponseDto();

        addressResponseDto.setCity( entity.getCity() );
        addressResponseDto.setCountry( entity.getCountry() );
        addressResponseDto.setCreatedAt( entity.getCreatedAt() );
        addressResponseDto.setId( entity.getId() );
        addressResponseDto.setStreet( entity.getStreet() );
        addressResponseDto.setType( entity.getType() );
        addressResponseDto.setZip( entity.getZip() );

        return addressResponseDto;
    }

    @Override
    public void updateEntity(AddressRequestDto dto, Address entity) {
        if ( dto == null ) {
            return;
        }

        entity.setCity( dto.getCity() );
        entity.setCountry( dto.getCountry() );
        entity.setStreet( dto.getStreet() );
        entity.setType( dto.getType() );
        entity.setZip( dto.getZip() );
    }
}
