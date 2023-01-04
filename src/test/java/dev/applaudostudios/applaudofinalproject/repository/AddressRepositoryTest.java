package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
public class AddressRepositoryTest {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    private Validator validator;
    private Address address1, address2, address3;
    private User user, user2;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = User.builder()
                .sid("1234")
                .email("cmartinez@gmail.com")
                .username("chrisjosuel")
                .firstName("Chris")
                .lastName("Martinez")
                .telephone("+50498398923")
                .status(true)
                .build();

        user2 = User.builder()
                .sid("5678")
                .email("temporal@gmail.com")
                .username("temporal")
                .firstName("Temp")
                .lastName("Martinez")
                .telephone("+50498498923")
                .status(true)
                .build();

        address1 = Address.builder()
                .street("Barrio Torondon")
                .city("Comayagua")
                .state("Comayagua")
                .country("Honduras")
                .zipCode("12101")
                .user(user)
                .status(true)
                .isDefault(true)
                .build();

        address2 = Address.builder()
                .street("Barrio La Cruz")
                .city("Lejamani")
                .state("Comayagua")
                .country("Honduras")
                .zipCode("12101")
                .user(user)
                .status(false)
                .isDefault(false)
                .build();

        address3 = Address.builder()
                .street("Barrio San Antonio")
                .city("Ajuterique")
                .state("Comayagua")
                .country("Honduras")
                .zipCode("12101")
                .user(user2)
                .status(false)
                .isDefault(false)
                .build();

        userRepository.saveAll(List.of(user, user2));
        addressRepository.saveAll(List.of(address1, address2, address3));
    }

    @AfterEach
    void tearDown() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("My JPA Queries")
    class MyJpaTest {

        @Test
        @DisplayName("FindByIdAndUserSidInStatusIsTrue With an Existing Address")
        void givenIdAddress_WhenFindByIdWithExistingUserAddress_thenAddressIsPresent() {
            Optional<Address> addressFound = addressRepository.findByIdAndStatusIsTrueAndUserSid(
                    address1.getId(),
                    address1.getUser().getSid());

            Assertions.assertTrue(addressFound.isPresent());
            assertThat(addressFound.get())
                    .usingRecursiveComparison()
                    .isEqualTo(address1);
        }

        @Test
        @DisplayName("FindByIdAndUserSidInStatusIsTrue With an Non Existing Address By User")
        void givenIdAddress_WhenFindByIdWithNonExistingUserAddress_thenAddressIsEmpty() {
            Optional<Address> addressFound = addressRepository.findByIdAndStatusIsTrueAndUserSid(
                    address3.getId(),
                    address1.getUser().getSid());

            Assertions.assertTrue(addressFound.isEmpty());
        }

        @Test
        @DisplayName("FindAddress and verify user's default address")
        void givenUserSid_WhenFindAddressWithUserDefault_thenDefaultIsPresent() {
            Optional<Address> addressFound = addressRepository.findAddress(address1.getUser().getSid());
            Assertions.assertTrue(addressFound.isPresent());
            assertThat(addressFound.get())
                    .usingRecursiveComparison()
                    .isEqualTo(address1);
        }

        @Test
        @DisplayName("FindAddress and verify user doesn't have a default payment")
        void givenUserSid_WhenFindAddressWithUserNoDefault_thenDefaultIsNotPresent() {
            Optional<Address> addressFound = addressRepository.findAddress(address3.getUser().getSid());
            assertThat(addressFound).isEmpty();
        }

        @Test
        @DisplayName("FindAllByUserSid With Active Address")
        void givenUserSid_WhenFindActiveAddress_thenAddressListSizeWithStatusIsTrue() {
            List<Address> myAddresses = addressRepository.findAllByUserSidAndStatusIsTrue(address1.getUser().getSid());
            assertThat(myAddresses.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("FindAllByUserSid With Active Payment Methods with paginable")
        void givenUserSid_WhenFindActiveCards_thenPaymentPaginationListSizeWithStatusIsTrue() {
            List<Address> myAddresses = addressRepository.findAllByUserSidAndStatusIsTrue(
                    address1.getUser().getSid(),
                    PageRequest.of(0, 1));
            assertThat(myAddresses.size()).isEqualTo(1);
            assertThat(myAddresses.get(0).getStreet()).isEqualTo("Barrio Torondon");
        }

        @Test
        @DisplayName("FindAllByUserSid with an non existing User")
        void givenInvalidUserSid_WhenFindActiveCards_thenResturnEmptyList() {
            List<Address> myAddresses = addressRepository.findAllByUserSidAndStatusIsTrue(anyString());
            assertThat(myAddresses.size()).isEqualTo(0);
        }

    }

    @Nested
    @DisplayName("JPA Implementations | Exceptions")
    class JpaImplementations {
        @Test
        @DisplayName("Save an Address with incorrect Data Length get Constraint Exceptions")
        void givenAddressData_WhenSaveAddressWithInvalidDataLength_thenConstraintSetList() {
            Address addressTest = Address.builder()
                    .street("")
                    .city("")
                    .state("")
                    .country("Honduras")
                    .zipCode("32")
                    .user(user)
                    .status(true)
                    .isDefault(true)
                    .build();

            Set<ConstraintViolation<Address>> violations = validator.validate(addressTest);
            assertThat(violations.size()).isEqualTo(6);
        }
    }

}
