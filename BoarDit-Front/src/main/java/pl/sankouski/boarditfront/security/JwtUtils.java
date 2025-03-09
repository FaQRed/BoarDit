package pl.sankouski.boarditfront.security;

import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JwtUtils {

    public static Map<String, Object> decodeJwt(String jwt) {
        try {

            String token = jwt.startsWith("Bearer ") ? jwt.substring(7) : jwt;
            String[] parts = token.split("\\.");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format.");
            }


            String payload = new String(Base64.getDecoder().decode(parts[1]));


            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(payload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode JWT token.", e);
        }
    }
}