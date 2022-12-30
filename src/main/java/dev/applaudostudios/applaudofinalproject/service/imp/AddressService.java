package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.entity.Address;
import dev.applaudostudios.applaudofinalproject.entity.User;
import dev.applaudostudios.applaudofinalproject.repository.AddressRepository;
import dev.applaudostudios.applaudofinalproject.service.IAddressService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import dev.applaudostudios.applaudofinalproject.utils.helpers.InfoCredential;
import dev.applaudostudios.applaudofinalproject.utils.helpers.ObjectNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private InfoCredential infoCredential;

    @Autowired
    private ObjectNull objectNull;

    @Override
    public List<Address> findAll(Integer from, Integer limit, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);

        List<Address> allAddress = addressRepository.findAllByUserSidAndStatusIsTrue(currentLoggedUser.getSid());

        if (limit == null || from == null) {
            return allAddress;
        }

        if (limit < 0 || from < 0) {
            throw new MyBusinessException("Limit and From must be greater than zero.", HttpStatus.BAD_REQUEST);
        }

        allAddress = addressRepository.findAllByUserSidAndStatusIsTrue(
                currentLoggedUser.getSid(),
                PageRequest.of(from, limit));

        return allAddress;
    }

    @Override
    public Address findAddressById(Long id, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        return findUserAddress(id, currentLoggedUser);
    }

    @Override
    public Address updateAddress(Long id, AddressDto addressDto, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        Address addressFound = findUserAddress(id, currentLoggedUser);

        Optional<Address> currentDefaultAddress = addressRepository.findAddress(currentLoggedUser.getSid());

        if(addressDto.isDefault() && currentDefaultAddress.isPresent()) {
            currentDefaultAddress.get().setDefault(false);
            addressRepository.save(currentDefaultAddress.get());
        }

        addressFound.setStreet(addressDto.getStreet());
        addressFound.setCity(addressDto.getCity());
        addressFound.setState(addressDto.getState());
        addressFound.setZipCode(addressDto.getZipCode());
        addressFound.setDefault(addressDto.isDefault());
        addressRepository.save(addressFound);

        return addressFound;
    }

    @Override
    public Address createAddress(AddressDto addressDto, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);

        addressDto.setUser(currentLoggedUser);
        Address newAddress = addressFromDto(addressDto);

        Optional<Address> currentDefaultAddress = addressRepository.findAddress(currentLoggedUser.getSid());

        if(newAddress.isDefault() && currentDefaultAddress.isPresent()) {
            currentDefaultAddress.get().setDefault(false);
            addressRepository.save(currentDefaultAddress.get());
        }

        addressRepository.save(newAddress);
        return newAddress;
    }

    @Override
    public Object deleteAddress(Long id, String username) {
        User currentLoggedUser = infoCredential.findUserInSession(username);
        Address addressFound = findUserAddress(id, currentLoggedUser);

        addressFound.setStatus(false);
        addressRepository.save(addressFound);

        return objectNull.getObjectNull();
    }

    private Address findUserAddress(Long id, User loggedUser) {
        Optional<Address> addressFound = addressRepository.findByIdAndStatusIsTrueAndUserSid(id, loggedUser.getSid());

        if (addressFound.isEmpty()) {
            throw new MyBusinessException("Current user doesn't have an address with given id.", HttpStatus.FORBIDDEN);
        }

        return addressFound.get();
    }

    private Address addressFromDto(AddressDto addressDto) {
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
