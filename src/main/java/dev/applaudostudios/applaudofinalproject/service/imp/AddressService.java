package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.AddressRepository;
import dev.applaudostudios.applaudofinalproject.service.IAddressService;
import dev.applaudostudios.applaudofinalproject.utils.helpers.db.AddressHelper;
import dev.applaudostudios.applaudofinalproject.utils.helpers.db.UserHelper;
import dev.applaudostudios.applaudofinalproject.utils.helpers.patterns.ObjectNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService implements IAddressService {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserHelper userHelper;
    @Autowired
    private AddressHelper addressHelper;
    @Autowired
    private ObjectNull objectNull;

    @Override
    public List<Address> findAll(Integer from, Integer limit, String username) {
        User currentLoggedUser = userHelper.findUserInSession(username);

        List<Address> allAddress = addressRepository.findAllByUserSidAndStatusIsTrue(currentLoggedUser.getSid());

        if (limit == null || from == null) {
            return allAddress;
        }

        allAddress = addressRepository.findAllByUserSidAndStatusIsTrue(
                currentLoggedUser.getSid(),
                PageRequest.of(from, limit));

        return allAddress;
    }

    @Override
    public Address findAddressById(Long id, String username) {
        User currentLoggedUser = userHelper.findUserInSession(username);
        return addressHelper.findUserAddress(id, currentLoggedUser);
    }

    @Override
    public Address updateAddress(Long id, AddressDto addressDto, String username) {
        User currentLoggedUser = userHelper.findUserInSession(username);
        Address addressFound = addressHelper.findUserAddress(id, currentLoggedUser);

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
        User currentLoggedUser = userHelper.findUserInSession(username);

        addressDto.setUser(currentLoggedUser);
        Address newAddress = addressHelper.addressFromDto(addressDto);

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
        User currentLoggedUser = userHelper.findUserInSession(username);
        Address addressFound = addressHelper.findUserAddress(id, currentLoggedUser);

        addressFound.setStatus(false);
        addressRepository.save(addressFound);

        return objectNull.getObjectNull();
    }
}
