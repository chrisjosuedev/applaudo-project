package dev.applaudostudios.applaudofinalproject.service;

import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.entity.Address;

import java.util.List;

public interface IAddressService {
    Address createAddress(AddressDto addressDto, String username);

    List<Address> findAll(Integer from, Integer limit, String username);

    Address findAddressById(Long id, String username);

    Address updateAddress(Long id, AddressDto addressDto, String username);

    List<Address> deleteAddress(Long id, String username);
}
