package dev.applaudostudios.applaudofinalproject.repository;

import dev.applaudostudios.applaudofinalproject.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import javax.validation.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private Validator validator;
    private User user, user2, user3, user4, user5;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = User.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .status(true)
                .build();

        user2 = User.builder()
                .sid("5678")
                .username("maria.lara")
                .firstName("Maria")
                .lastName("Lara")
                .telephone("+50487828220")
                .email("maria@gmail.com")
                .status(true)
                .build();

        user3 = User.builder()
                .sid("9101")
                .username("angel.lara")
                .firstName("Angel")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("angel@gmail.com")
                .status(false)
                .build();

        user4 = User.builder()
                .sid(UUID.randomUUID().toString())
                .username("angel.lara")
                .firstName("Angel")
                .lastName("Martinez")
                .email("notvalidemail.com")
                .status(false)
                .build();

        user5 = User.builder()
                .sid("9089")
                .username("carmen.lara")
                .firstName("Carmen")
                .lastName("Martinez")
                .telephone("+50498980093")
                .email("valid@gmail.com")
                .status(true)
                .build();

        userRepository.saveAll(List.of(user, user2, user3));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("My JPA Queries Tests")
    class MyJpaTest {
        @Test
        @DisplayName("FindBySid With Existing User")
        void givenSid_whenFindBySidWithAExistingUser_thenUserIsPresent() {

            Optional<User> userFoundBySid = userRepository.findBySid("1234");

            assertTrue(userFoundBySid.isPresent());
            assertThat(userFoundBySid.get())
                    .usingRecursiveComparison()
                    .isEqualTo(user);
        }

        @Test
        @DisplayName("FindBySid with a Non Existing User")
        void givenSid_whenFindBySidWithANonExistingUser_thenUserIsNotPresent() {
            Optional<User> userFoundBySid = userRepository.findBySid(anyString());
            assertThat(userFoundBySid).isNotPresent();
        }

        @Test
        @DisplayName("FindByUsername With Existing User")
        void givenUsername_whenFindByUsernameWithAExistingUser_thenUserIsPresent() {
            Optional<User> userFoundByUsername = userRepository.findByUsername("chrisjosuel");

            assertTrue(userFoundByUsername.isPresent());

            assertThat(userFoundByUsername.get())
                    .usingRecursiveComparison()
                    .isEqualTo(user);
        }

        @Test
        @DisplayName("FindByUsername with a Non Existing User")
        void givenUsername_whenFindBySidWithANonExistingUser_thenUserIsNotPresent() {
            Optional<User> userFoundByUsername = userRepository.findByUsername(anyString());
            assertThat(userFoundByUsername).isNotPresent();
        }

        @Test
        @DisplayName("FinAllByStatusIsTrue active users list")
        void givenUsers_whenFindAll_thenUserListSizeWithStatusTrue() {
            List<User> usersList = userRepository.findAllByStatusIsTrue();
            assertThat(usersList.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("FindAllByStatusIsTrue active users list with pageable")
        void givenUsers_whenFindAllWithPagination_thenUserPaginationListSizeWithStatusTrue() {
            List<User> usersList = userRepository.findAllByStatusIsTrue(PageRequest.of(0, 1));
            assertThat(usersList.size()).isEqualTo(1);
            assertThat(usersList.get(0).getFirstName()).isEqualTo("Christhian");
        }
    }

    @Nested
    @DisplayName("JPA Implementations")
    class JpaImplementations {
        @Test
        @DisplayName("Save an User with incorrect Email get Constraint Exceptions")
        void givenUserData_WhenSaveUserWithInvalidEmail_thenConstraintSetList() {
            Set<ConstraintViolation<User>> violations = validator.validate(user4);
            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("Save User with valid data")
        void givenUserData_WhenSaveUser_thenSaveCorrectValidData() {
            User userSaved = userRepository.save(user5);
            assertThat(userSaved).isNotNull();
        }
    }
}
