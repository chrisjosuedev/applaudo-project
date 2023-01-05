package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.helpers.db.UserHelper;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.repository.UserRepository;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserHelper userHelper;
    private User user;
    private UserDto userDto;
    private UserUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .status(true)
                .build();

        userDto = UserDto.builder()
                .sid(user.getSid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .telephone(user.getTelephone())
                .username(user.getUsername())
                .build();

        updateDto = UserUpdateDto.builder()
                .firstName("New name")
                .lastName("New last name")
                .telephone("+50490893849")
                .build();
    }

    @Nested
    @DisplayName("User Service Tests")
    class UserServiceImpTest {
        @Test
        @DisplayName("FindAllUsers without Pagination")
        void givenUser_WhenFindAllUsersWithNoPagination_thenReturnList() {
            given(userRepository.findAllByStatusIsTrue()).willReturn(List.of(user));
            List<User> allUsers = userService.findAll(null, null);
            assertThat(allUsers.size()).isGreaterThan(0);
            assertThat(allUsers.get(0).getFirstName()).isEqualTo("Christhian");
        }

        @Test
        @DisplayName("FindAllUsers with Pagination")
        void givenUser_WhenFindAllUsersWithPagination_thenReturnPaginationList() {
            given(userRepository.findAllByStatusIsTrue(
                    PageRequest.of(1, 1)
            )).willReturn(List.of(user));
            List<User> allUsers = userService.findAll(1, 1);
            assertThat(allUsers.size()).isGreaterThan(0);
            assertThat(allUsers.get(0).getFirstName()).isEqualTo("Christhian");
        }

        @Test
        @DisplayName("Updating an user")
        void givenUser_whenUpdateAnUser_thenReturnUserUpdated() {
            given(userRepository.save(user)).willReturn(user);
            given(userHelper.findUserInSession(user.getUsername())).willReturn(user);
            given(userHelper.userDtoResponse(user)).willReturn(userDto);

            userDto.setFirstName(updateDto.getFirstName());
            userDto.setLastName(updateDto.getLastName());
            userDto.setTelephone(updateDto.getTelephone());

            UserDto userDtoUpdated = userService.updateUser(userDto.getUsername(), updateDto);

            assertThat(userDtoUpdated.getFirstName()).isEqualTo("New name");
            assertThat(userDtoUpdated.getLastName()).isEqualTo("New last name");
            assertThat(userDtoUpdated.getTelephone()).isEqualTo("+50490893849");
        }

        @Test
        @DisplayName("Find user by username | Valid username")
        void givenUser_whenFindByUsernameWithValidUsername_thenReturnUser() {
            given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
            given(userHelper.userDtoResponse(user)).willReturn(userDto);

            UserDto userByUsername = userService.findByUsername(user.getUsername());

            assertThat(userByUsername).isNotNull();
        }
    }

    @Nested
    @DisplayName("User Service Tests | Throw an Exception")
    class UserServiceImpTestException {
        @Test
        @DisplayName("Find user by username | Invalid username Not Found")
        void givenUser_whenFindByUsernameWithInvalidUsername_thenThrowsAnException() {
            given(userRepository.findByUsername("string")).willReturn(Optional.empty());

            assertThrows(MyBusinessException.class, () -> {
                userService.findByUsername("string");
            });
        }
    }
}