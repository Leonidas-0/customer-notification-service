package com.levanz.customer.mapper;

import com.levanz.customer.dto.AddressDto;
import com.levanz.customer.dto.CustomerDto;
import com.levanz.customer.dto.NotificationStatusDto;
import com.levanz.customer.dto.PreferenceDto;
import com.levanz.customer.entity.Address;
import com.levanz.customer.entity.Channel;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.entity.NotificationStatus;
import com.levanz.customer.entity.Preference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-08T00:55:21+0400",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerDto dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setAddresses( addressDtoListToAddressList( dto.getAddresses() ) );
        customer.setCreatedAt( dto.getCreatedAt() );
        customer.setEmail( dto.getEmail() );
        customer.setFirstName( dto.getFirstName() );
        customer.setId( dto.getId() );
        customer.setLastName( dto.getLastName() );
        customer.setNotifications( notificationStatusDtoListToNotificationStatusList( dto.getNotifications() ) );
        customer.setPreferences( preferenceDtoListToPreferenceList( dto.getPreferences() ) );

        return customer;
    }

    @Override
    public CustomerDto toDto(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        CustomerDto customerDto = new CustomerDto();

        customerDto.setAddresses( addressListToAddressDtoList( entity.getAddresses() ) );
        customerDto.setCreatedAt( entity.getCreatedAt() );
        customerDto.setEmail( entity.getEmail() );
        customerDto.setFirstName( entity.getFirstName() );
        customerDto.setId( entity.getId() );
        customerDto.setLastName( entity.getLastName() );
        customerDto.setNotifications( notificationStatusListToNotificationStatusDtoList( entity.getNotifications() ) );
        customerDto.setPreferences( preferenceListToPreferenceDtoList( entity.getPreferences() ) );

        return customerDto;
    }

    @Override
    public void updateEntity(CustomerDto dto, Customer entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getAddresses() != null ) {
            List<Address> list = addressDtoListToAddressList( dto.getAddresses() );
            if ( list != null ) {
                entity.getAddresses().clear();
                entity.getAddresses().addAll( list );
            }
            else {
                entity.setAddresses( null );
            }
        }
        else {
            List<Address> list = addressDtoListToAddressList( dto.getAddresses() );
            if ( list != null ) {
                entity.setAddresses( list );
            }
        }
        entity.setCreatedAt( dto.getCreatedAt() );
        entity.setEmail( dto.getEmail() );
        entity.setFirstName( dto.getFirstName() );
        entity.setId( dto.getId() );
        entity.setLastName( dto.getLastName() );
        if ( entity.getNotifications() != null ) {
            List<NotificationStatus> list1 = notificationStatusDtoListToNotificationStatusList( dto.getNotifications() );
            if ( list1 != null ) {
                entity.getNotifications().clear();
                entity.getNotifications().addAll( list1 );
            }
            else {
                entity.setNotifications( null );
            }
        }
        else {
            List<NotificationStatus> list1 = notificationStatusDtoListToNotificationStatusList( dto.getNotifications() );
            if ( list1 != null ) {
                entity.setNotifications( list1 );
            }
        }
        if ( entity.getPreferences() != null ) {
            List<Preference> list2 = preferenceDtoListToPreferenceList( dto.getPreferences() );
            if ( list2 != null ) {
                entity.getPreferences().clear();
                entity.getPreferences().addAll( list2 );
            }
            else {
                entity.setPreferences( null );
            }
        }
        else {
            List<Preference> list2 = preferenceDtoListToPreferenceList( dto.getPreferences() );
            if ( list2 != null ) {
                entity.setPreferences( list2 );
            }
        }
    }

    protected Address addressDtoToAddress(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address address = new Address();

        address.setId( addressDto.getId() );

        return address;
    }

    protected List<Address> addressDtoListToAddressList(List<AddressDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Address> list1 = new ArrayList<Address>( list.size() );
        for ( AddressDto addressDto : list ) {
            list1.add( addressDtoToAddress( addressDto ) );
        }

        return list1;
    }

    protected NotificationStatus notificationStatusDtoToNotificationStatus(NotificationStatusDto notificationStatusDto) {
        if ( notificationStatusDto == null ) {
            return null;
        }

        NotificationStatus notificationStatus = new NotificationStatus();

        notificationStatus.setId( notificationStatusDto.getId() );

        return notificationStatus;
    }

    protected List<NotificationStatus> notificationStatusDtoListToNotificationStatusList(List<NotificationStatusDto> list) {
        if ( list == null ) {
            return null;
        }

        List<NotificationStatus> list1 = new ArrayList<NotificationStatus>( list.size() );
        for ( NotificationStatusDto notificationStatusDto : list ) {
            list1.add( notificationStatusDtoToNotificationStatus( notificationStatusDto ) );
        }

        return list1;
    }

    protected Preference preferenceDtoToPreference(PreferenceDto preferenceDto) {
        if ( preferenceDto == null ) {
            return null;
        }

        Preference preference = new Preference();

        if ( preferenceDto.getChannel() != null ) {
            preference.setChannel( Enum.valueOf( Channel.class, preferenceDto.getChannel() ) );
        }
        preference.setId( preferenceDto.getId() );

        return preference;
    }

    protected List<Preference> preferenceDtoListToPreferenceList(List<PreferenceDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Preference> list1 = new ArrayList<Preference>( list.size() );
        for ( PreferenceDto preferenceDto : list ) {
            list1.add( preferenceDtoToPreference( preferenceDto ) );
        }

        return list1;
    }

    protected AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setId( address.getId() );

        return addressDto;
    }

    protected List<AddressDto> addressListToAddressDtoList(List<Address> list) {
        if ( list == null ) {
            return null;
        }

        List<AddressDto> list1 = new ArrayList<AddressDto>( list.size() );
        for ( Address address : list ) {
            list1.add( addressToAddressDto( address ) );
        }

        return list1;
    }

    protected NotificationStatusDto notificationStatusToNotificationStatusDto(NotificationStatus notificationStatus) {
        if ( notificationStatus == null ) {
            return null;
        }

        NotificationStatusDto notificationStatusDto = new NotificationStatusDto();

        notificationStatusDto.setId( notificationStatus.getId() );

        return notificationStatusDto;
    }

    protected List<NotificationStatusDto> notificationStatusListToNotificationStatusDtoList(List<NotificationStatus> list) {
        if ( list == null ) {
            return null;
        }

        List<NotificationStatusDto> list1 = new ArrayList<NotificationStatusDto>( list.size() );
        for ( NotificationStatus notificationStatus : list ) {
            list1.add( notificationStatusToNotificationStatusDto( notificationStatus ) );
        }

        return list1;
    }

    protected PreferenceDto preferenceToPreferenceDto(Preference preference) {
        if ( preference == null ) {
            return null;
        }

        PreferenceDto preferenceDto = new PreferenceDto();

        if ( preference.getChannel() != null ) {
            preferenceDto.setChannel( preference.getChannel().name() );
        }
        preferenceDto.setId( preference.getId() );

        return preferenceDto;
    }

    protected List<PreferenceDto> preferenceListToPreferenceDtoList(List<Preference> list) {
        if ( list == null ) {
            return null;
        }

        List<PreferenceDto> list1 = new ArrayList<PreferenceDto>( list.size() );
        for ( Preference preference : list ) {
            list1.add( preferenceToPreferenceDto( preference ) );
        }

        return list1;
    }
}
