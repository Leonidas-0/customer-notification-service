package com.levanz.customer.mapper;

import com.levanz.customer.dto.AddressResponseDto;
import com.levanz.customer.dto.CustomerRequestDto;
import com.levanz.customer.dto.CustomerResponseDto;
import com.levanz.customer.dto.NotificationResponseDto;
import com.levanz.customer.dto.PreferenceResponseDto;
import com.levanz.customer.entity.Address;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.entity.Notification;
import com.levanz.customer.entity.Preference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T00:30:15+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Eclipse Adoptium)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setFirstName( dto.getFirstName() );
        customer.setLastName( dto.getLastName() );
        customer.setEmail( dto.getEmail() );

        return customer;
    }

    @Override
    public CustomerResponseDto toDto(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerResponseDto customerResponseDto = new CustomerResponseDto();

        customerResponseDto.setId( entity.getId() );
        customerResponseDto.setFirstName( entity.getFirstName() );
        customerResponseDto.setLastName( entity.getLastName() );
        customerResponseDto.setEmail( entity.getEmail() );
        customerResponseDto.setCreatedAt( entity.getCreatedAt() );
        customerResponseDto.setAddresses( addressListToAddressResponseDtoList( entity.getAddresses() ) );
        customerResponseDto.setPreferences( preferenceListToPreferenceResponseDtoList( entity.getPreferences() ) );
        customerResponseDto.setNotifications( notificationListToNotificationResponseDtoList( entity.getNotifications() ) );

        return customerResponseDto;
    }

    @Override
    public void updateEntity(CustomerRequestDto dto, Customer entity) {
        if ( dto == null ) {
            return;
        }

        entity.setFirstName( dto.getFirstName() );
        entity.setLastName( dto.getLastName() );
        entity.setEmail( dto.getEmail() );
    }

    protected AddressResponseDto addressToAddressResponseDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponseDto addressResponseDto = new AddressResponseDto();

        addressResponseDto.setId( address.getId() );
        addressResponseDto.setType( address.getType() );
        addressResponseDto.setCountry( address.getCountry() );
        addressResponseDto.setCity( address.getCity() );
        addressResponseDto.setStreet( address.getStreet() );
        addressResponseDto.setZip( address.getZip() );
        addressResponseDto.setCreatedAt( address.getCreatedAt() );

        return addressResponseDto;
    }

    protected List<AddressResponseDto> addressListToAddressResponseDtoList(List<Address> list) {
        if ( list == null ) {
            return null;
        }

        List<AddressResponseDto> list1 = new ArrayList<AddressResponseDto>( list.size() );
        for ( Address address : list ) {
            list1.add( addressToAddressResponseDto( address ) );
        }

        return list1;
    }

    protected PreferenceResponseDto preferenceToPreferenceResponseDto(Preference preference) {
        if ( preference == null ) {
            return null;
        }

        PreferenceResponseDto preferenceResponseDto = new PreferenceResponseDto();

        preferenceResponseDto.setId( preference.getId() );
        preferenceResponseDto.setChannel( preference.getChannel() );
        preferenceResponseDto.setOptedIn( preference.isOptedIn() );
        preferenceResponseDto.setUpdatedAt( preference.getUpdatedAt() );

        return preferenceResponseDto;
    }

    protected List<PreferenceResponseDto> preferenceListToPreferenceResponseDtoList(List<Preference> list) {
        if ( list == null ) {
            return null;
        }

        List<PreferenceResponseDto> list1 = new ArrayList<PreferenceResponseDto>( list.size() );
        for ( Preference preference : list ) {
            list1.add( preferenceToPreferenceResponseDto( preference ) );
        }

        return list1;
    }

    protected NotificationResponseDto notificationToNotificationResponseDto(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();

        notificationResponseDto.setId( notification.getId() );
        notificationResponseDto.setStatus( notification.getStatus() );
        notificationResponseDto.setCreatedAt( notification.getCreatedAt() );
        notificationResponseDto.setUpdatedAt( notification.getUpdatedAt() );

        return notificationResponseDto;
    }

    protected List<NotificationResponseDto> notificationListToNotificationResponseDtoList(List<Notification> list) {
        if ( list == null ) {
            return null;
        }

        List<NotificationResponseDto> list1 = new ArrayList<NotificationResponseDto>( list.size() );
        for ( Notification notification : list ) {
            list1.add( notificationToNotificationResponseDto( notification ) );
        }

        return list1;
    }
}
