package dev.applaudostudios.applaudofinalproject.helpers.jwt;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Base64;

public class JwtDecoder {
    private static final Logger logger = LoggerFactory.getLogger(JwtDecoder.class);

    public static UserDto getUserInfo(String token) {
        String[] parts = token.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        return UserDto.builder()
                .sid(payload.getString("sub"))
                .firstName(getFirstName(payload))
                .lastName(getLastName(payload))
                .email(getEmail(payload))
                .telephone("")
                .username(payload.getString("preferred_username"))
                .build();
    }

    public static AccessToken userCredentials(Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        return keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
    }

    private static String getFirstName(JSONObject payload) {
        try {
            return payload.getString("given_name");
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return "";
        }
    }

    private static String getLastName(JSONObject payload) {
        try {
            return payload.getString("family_name");
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return "";
        }
    }

    private static String getEmail(JSONObject payload) {
        try {
            return payload.getString("email");
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return "";
        }
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

}
