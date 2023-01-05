package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.helpers.db.AddressHelper;
import dev.applaudostudios.applaudofinalproject.helpers.db.UserHelper;
import dev.applaudostudios.applaudofinalproject.helpers.patterns.ObjectNull;
import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.AddressRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AddressServiceTest {
    @InjectMocks
    private AddressService addressService;
    @Mock
    private AddressHelper addressHelper;
    @Mock
    private UserHelper userHelper;
    @Mock
    private ObjectNull objectNull;
    @Mock
    private AddressRepository addressRepository;
    private User user;
    private Address address;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .sid("1234")
                .email("cmartinez@gmail.com")
                .username("chrisjosuel")
                .firstName("Chris")
                .lastName("Martinez")
                .telephone("+50498398923")
                .status(true)
                .build();

        addressDto = AddressDto.builder()
                .street("Barrio Torondon")
                .city("New City")
                .state("Comayagua")
                .country("Honduras")
                .zipCode("12101")
                .user(user)
                .isDefault(true)
                .build();

        address = Address.builder()
                .id(1L)
                .country(addressDto.getCountry())
                .state(addressDto.getState())
                .street("Comayagua")
                .city(addressDto.getCity())
                .zipCode(addressDto.getZipCode())
                .user(addressDto.getUser())
                .status(true)
                .isDefault(addressDto.isDefault())
                .build();
    }

    @Nested
    @DisplayName("Address Service Tests")
    class AddressServiceImpTest {
        @Test
        @DisplayName("FindAllAddress without Pagination")
        void givenUser_WhenFindAllProductsWithNoPagination_thenReturnList() {
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressRepository.findAllByUserSidAndStatusIsTrue(
                    user.getSid()
            )).willReturn(List.of(address));

            List<Address> allAddress = addressService.findAll(null, null, user.getUsername());

            assertThat(allAddress.size()).isGreaterThan(0);
            assertThat(allAddress.get(0).getCountry()).isEqualTo("Honduras");
        }

        @Test
        @DisplayName("FindAllAddress with Pagination")
        void givenUser_WhenFindAllProductsWithPagination_thenReturnPaginationList() {
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressRepository.findAllByUserSidAndStatusIsTrue(
                    user.getSid(),
                    PageRequest.of(1, 1)
            )).willReturn(List.of(address));

            List<Address> allAddress = addressService.findAll(1, 1, user.getUsername());

            assertThat(allAddress.size()).isGreaterThan(0);
            assertThat(allAddress.get(0).getCountry()).isEqualTo("Honduras");
        }
        @Test
        @DisplayName("Find an Address By Id with Existing Address Id")
        void givenAnAddressId_whenFindAddressById_thenReturnAddressFound() {
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.findUserAddress(address.getId(), user)).willReturn(address);

            Address addressFound = addressService.findAddressById(address.getId(), user.getUsername());

            assertThat(addressFound).isNotNull();
            assertThat(addressFound.getCountry()).isEqualTo("Honduras");
        }
        @Test
        @DisplayName("Create an Address with Existing User")
        void givenAnAddress_whenCreateANewAddressWithValidData_thenReturnAddressNotNull() {
            given(addressRepository.save(address)).willReturn(address);
            given(addressRepository.findAddress(user.getSid())).willReturn(Optional.of(address));
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.addressFromDto(addressDto)).willReturn(address);

            Address newAddress = addressService.createAddress(addressDto, user.getUsername());

            assertThat(newAddress).isNotNull();
        }

        @Test
        @DisplayName("Update an Address with Existing User and Address")
        void givenAnAddress_whenUpdateAnAddressWithValidData_thenReturnAddressNotNull() {
            given(addressRepository.save(address)).willReturn(address);
            given(addressRepository.findAddress(user.getSid())).willReturn(Optional.of(address));
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.findUserAddress(address.getId(), address.getUser())).willReturn(address);
            given(addressHelper.addressFromDto(addressDto)).willReturn(address);

            address.setCity(addressDto.getCity());

            Address updatedAddress = addressService.updateAddress(
                    address.getId(), addressDto,
                    address.getUser().getUsername());

            assertThat(updatedAddress).isNotNull();
            assertThat(updatedAddress.getCity()).isEqualTo("New City");
        }

        @Test
        @DisplayName("Delete an Address with Valid id")
        void givenAnAddressId_whenDeleteAddress_thenReturnObjectNull() {
            given(addressRepository.save(address)).willReturn(address);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.findUserAddress(address.getId(), user)).willReturn(address);
            given(objectNull.getObjectNull()).willReturn(Collections.emptyList());

            Object removedAddress = addressService.deleteAddress(address.getId(), user.getUsername());

            assertEquals(Collections.emptyList(), removedAddress);
        }
    }

    @Nested
    @DisplayName("Address Service Tests | Throw an Exception")
    class AddressServiceImpTestException {
        @Test
        @DisplayName("Find an Address By Id with Non Existing Address Id User")
        void givenAnAddressId_whenFindAddressById_thenReturnAddressNull() {
            given(userHelper.findUserInSession(user.getUsername())).willThrow(MyBusinessException.class);

            assertThrows(MyBusinessException.class, () -> {
                addressService.findAddressById(address.getId(), user.getUsername());
            });

            verify(addressHelper, never()).findUserAddress(address.getId(), user);
        }
        @Test
        @DisplayName("Create an Address with non Existing User | Service Null")
        void givenAnAddress_whenCreateANewAddressWithValidData_thenReturnAddressNull() {
            given(addressRepository.save(address)).willReturn(address);
            given(userHelper.findUserInSession("random")).willThrow(MyBusinessException.class);

            assertThrows(NullPointerException.class, () -> {
                addressService.createAddress(addressDto, user.getUsername());
            });
        }

        @Test
        @DisplayName("Update-Delete an Address with Non Existing User")
        void givenAnAddress_whenUpdateAnAddressWithInValidUserData_thenReturnAddressNull() {
            given(addressRepository.save(address)).willReturn(address);
            given(userHelper.findUserInSession("random")).willThrow(MyBusinessException.class);

            address.setCity(addressDto.getCity());

            assertThrows(NullPointerException.class, () -> {
                addressService.updateAddress(
                        address.getId(), addressDto,
                        address.getUser().getUsername());
            });
        }

        @Test
        @DisplayName("Update an Address with Non Existing Address")
        void givenAnAddress_whenUpdateAnAddressWithInValidAddressData_thenReturnAddressNull() {
            given(addressRepository.save(address)).willReturn(address);
            given(addressRepository.findAddress(user.getSid())).willReturn(Optional.of(address));
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(addressHelper.findUserAddress(address.getId(), address.getUser())).willThrow(MyBusinessException.class);

            address.setCity(addressDto.getCity());

            assertThrows(MyBusinessException.class, () -> {
                addressService.updateAddress(
                        address.getId(), addressDto,
                        address.getUser().getUsername());
            });
        }
    }
}