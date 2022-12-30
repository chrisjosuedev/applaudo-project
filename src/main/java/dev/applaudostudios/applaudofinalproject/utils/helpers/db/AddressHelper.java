package dev.applaudostudios.applaudofinalproject.utils.helpers.db;

import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.AddressRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressHelper {
    @Autowired
    private AddressRepository addressRepository;

    public Address findUserAddress(Long id, User loggedUser) {
        Optional<Address> addressFound = addressRepository.findByIdAndStatusIsTrueAndUserSid(id, loggedUser.getSid());

        if (addressFound.isEmpty()) {
            throw new MyBusinessException("Current user doesn't have an address with given id.", HttpStatus.FORBIDDEN);
        }

        return addressFound.get();
    }

    public Address addressFromDto(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .state(addressDto.getState())
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .zipCode(addressDto.getZipCode())
                .user(addressDto.getUser())
                .status(true)
                .isDefault(addressDto.isDefault())
                .build();
    }

}
