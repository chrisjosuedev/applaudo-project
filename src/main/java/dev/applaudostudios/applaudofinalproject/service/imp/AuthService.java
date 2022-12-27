package dev.applaudostudios.applaudofinalproject.service.imp;

import dev.applaudostudios.applaudofinalproject.dto.auth.LoginReqDto;
import dev.applaudostudios.applaudofinalproject.dto.auth.LoginResDto;
import dev.applaudostudios.applaudofinalproject.dto.general.ResponseDto;
import dev.applaudostudios.applaudofinalproject.dto.auth.TokenReqDto;
import dev.applaudostudios.applaudofinalproject.service.IUserService;
import dev.applaudostudios.applaudofinalproject.utils.exceptions.MyBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AuthService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private IUserService userService;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String tokenUrl;

    @Value("${variables.logout.uri}")
    private String logoutUrl;

    public LoginResDto login(LoginReqDto loginRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);

        map.add("username", loginRequest.getUsername());
        map.add("password", loginRequest.getPassword());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<LoginResDto> response = restTemplate.postForEntity(
                tokenUrl,
                httpEntity,
                LoginResDto.class);

        userService.createUser(Objects.requireNonNull(response.getBody()).getAccess_token());
        return response.getBody();

    }

    public ResponseDto logout(TokenReqDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", request.getToken());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        ResponseEntity<ResponseDto> response = restTemplate.postForEntity(logoutUrl, httpEntity, ResponseDto.class);

        if(!response.getStatusCode().is2xxSuccessful()) {
            throw new MyBusinessException("Logout fail.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseDto.builder()
                .message("Logged out successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
