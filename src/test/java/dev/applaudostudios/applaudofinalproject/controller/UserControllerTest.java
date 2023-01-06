package dev.applaudostudios.applaudofinalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import dev.applaudostudios.applaudofinalproject.dto.entities.UserUpdateDto;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.service.imp.UserService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(UserController.class)
@Import(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private Principal principal;
    @MockBean
    private JwtDecoder jwtDecoder;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;
    private UserDto userDto;
    private UserUpdateDto userUpdateDto, userUpdateDtoInvalid;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .sid("1234")
                .username("chrisjosuel")
                .firstName("Christhian")
                .lastName("Martinez")
                .telephone("+50497828220")
                .email("cmartinez@gmail.com")
                .build();

        user = User.builder()
                .sid(userDto.getSid())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .status(true)
                .telephone(userDto.getTelephone())
                .username(userDto.getUsername())
                .build();

        userUpdateDto = UserUpdateDto.builder()
                .firstName("Maria")
                .lastName("Martinez")
                .telephone("+5088148614")
                .build();

        userUpdateDtoInvalid = UserUpdateDto.builder().build();
    }

    @Nested
    @DisplayName("User Controller Test")
    class UserControllerApiTest {
        @Test
        @DisplayName("findByUsername then return Status OK")
        void givenUsername_whenFindByUsername_thenReturnOK() throws Exception {
            given(userService.findByUsername(user.getUsername())).willReturn(userDto);
            ResultActions response = mockMvc.perform(get(
                    "/users/username/chrisjosuel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userDto))
            );
            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstName", CoreMatchers.is(userDto.getFirstName())));
        }

        @Test
        @DisplayName("UpdateUser then return Status OK")
        void givenUser_whenUpdateUser_thenReturnOK() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(userService.updateUser(user.getUsername(), userUpdateDto))
                    .willReturn(userDto);

            ResultActions response = mockMvc.perform(put("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userUpdateDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("User Controller Test Exceptions")
    class UserControllerApiTestException {
        @Test
        @DisplayName("UpdateUser Invalid Data returns Bad Request")
        void givenUser_whenUpdateUser_thenReturnBadRequest() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(userService.updateUser(user.getUsername(), userUpdateDtoInvalid))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(put("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userUpdateDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }
}