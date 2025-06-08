package com.levanz.customer.mapper;

import com.levanz.customer.dto.CustomerRequestDto;
import com.levanz.customer.dto.CustomerResponseDto;
import com.levanz.customer.entity.Customer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-08T19:30:01+0400",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setEmail( dto.getEmail() );
        customer.setFirstName( dto.getFirstName() );
        customer.setLastName( dto.getLastName() );

        return customer;
    }

    @Override
    public CustomerResponseDto toDto(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerResponseDto customerResponseDto = new CustomerResponseDto();

        customerResponseDto.setCreatedAt( entity.getCreatedAt() );
        customerResponseDto.setEmail( entity.getEmail() );
        customerResponseDto.setFirstName( entity.getFirstName() );
        customerResponseDto.setId( entity.getId() );
        customerResponseDto.setLastName( entity.getLastName() );

        return customerResponseDto;
    }

    @Override
    public void updateEntity(CustomerRequestDto dto, Customer entity) {
        if ( dto == null ) {
            return;
        }

        entity.setEmail( dto.getEmail() );
        entity.setFirstName( dto.getFirstName() );
        entity.setLastName( dto.getLastName() );
    }
}
