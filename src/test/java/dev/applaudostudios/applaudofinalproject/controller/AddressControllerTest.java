package dev.applaudostudios.applaudofinalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.applaudostudios.applaudofinalproject.dto.entities.AddressDto;
import dev.applaudostudios.applaudofinalproject.helpers.jwt.JwtDecoder;
import dev.applaudostudios.applaudofinalproject.models.Address;
import dev.applaudostudios.applaudofinalproject.models.User;
import dev.applaudostudios.applaudofinalproject.service.imp.AddressService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AddressController.class)
@Import(AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AddressControllerTest {
    @Mock
    private Principal principal;
    @MockBean
    private JwtDecoder jwtDecoder;
    @MockBean
    private AddressService addressService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private User user;
    private Address address;
    private AddressDto addressDto, addressDtoInvalid;

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

        addressDtoInvalid = AddressDto.builder().build();
    }

    @Nested
    @DisplayName("Address Controller Test")
    class AddressControllerApiTest {
        @Test
        @DisplayName("Get Address then return Status OK")
        void givenAddress_whenGetAddress_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(addressService.findAddressById(address.getId(), user.getUsername()))
                    .willReturn(address);

            ResultActions response = mockMvc.perform(get("/addresses/1")
                    .contentType(MediaType.APPLICATION_JSON)
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address found.")));
        }
        @Test
        @DisplayName("CreateAddress then return Status Created")
        void givenAddress_whenCreateAddress_thenReturnCreated() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(addressService.createAddress(addressDto, user.getUsername()))
                    .willReturn(address);

            ResultActions response = mockMvc.perform(post("/addresses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addressDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address created successfully.")));
        }

        @Test
        @DisplayName("UpdateAddress then return Status OK")
        void givenAddress_whenUpdateAddress_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(addressService.updateAddress(address.getId(), addressDto, user.getUsername()))
                    .willReturn(address);

            ResultActions response = mockMvc.perform(put("/addresses/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addressDto))
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address updated successfully.")));
        }

        @Test
        @DisplayName("Delete Address then return Status OK")
        void givenAddress_whenDeleteAddress_thenReturnOk() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(addressService.deleteAddress(address.getId(), user.getUsername()))
                    .willReturn(address);

            ResultActions response = mockMvc.perform(delete("/addresses/1")
                    .contentType(MediaType.APPLICATION_JSON)
            );

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Address removed successfully.")));
        }
    }

    @Nested
    @DisplayName("Address Controller Test Exception")
    class AddressControllerApiTestException {
        @Test
        @DisplayName("CreateAddress then return Status Bad Request")
        void givenAddress_whenCreateAddress_thenReturnBadRequest() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(addressService.createAddress(addressDtoInvalid, user.getUsername()))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(post("/addresses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addressDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        @DisplayName("UpdateAddress then return Status Bad Request")
        void givenAddress_whenUpdateAddress_thenReturnBadRequest() throws Exception {

            given(jwtDecoder.userCredentials(principal)).willReturn(user.getUsername());
            given(addressService.updateAddress(address.getId(), addressDtoInvalid, user.getUsername()))
                    .willThrow(MyBusinessException.class);

            ResultActions response = mockMvc.perform(put("/addresses/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addressDtoInvalid))
            );

            response.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }

}