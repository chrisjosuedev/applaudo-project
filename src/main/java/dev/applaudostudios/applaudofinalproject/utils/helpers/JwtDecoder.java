package dev.applaudostudios.applaudofinalproject.utils.helpers;

import dev.applaudostudios.applaudofinalproject.dto.entities.UserDto;
import org.slf4j.Logger;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtDecoder {
    private static final Logger logger = LoggerFactory.getLogger(JwtDecoder.class);

    public UserDto getUserInfo(String token) {
        String[] parts = token.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        return UserDto.builder()
                .sid(payload.getString("sub"))
                .firstName(getFirstName(payload))
                .lastName(getLastName(payload))
                .email(getEmail(payload))
                .username(payload.getString("preferred_username"))
                .build();
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
