package pl.sankouski.boarditfront.security;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    @Test
    void testDecodeJwt_ValidToken() {
       // {"sub": "1234567890", "name": "John Doe", "admin": true}
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9."
                + "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        Map<String, Object> result = JwtUtils.decodeJwt(jwt);

        assertNotNull(result);
        assertEquals("1234567890", result.get("sub"));
        assertEquals("John Doe", result.get("name"));
        assertTrue((Boolean) result.get("admin"));
    }

    @Test
    void testDecodeJwt_ValidTokenWithBearerPrefix() {
        String jwt = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9."
                + "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";


        Map<String, Object> result = JwtUtils.decodeJwt(jwt);


        assertNotNull(result);
        assertEquals("1234567890", result.get("sub"));
        assertEquals("John Doe", result.get("name"));
        assertTrue((Boolean) result.get("admin"));
    }

    @Test
    void testDecodeJwt_InvalidTokenFormat() {
        String invalidJwt = "invalid.token";

        Exception exception = assertThrows(RuntimeException.class, () -> JwtUtils.decodeJwt(invalidJwt));
        assertTrue(exception.getMessage().contains("Failed to decode JWT token."),
                "Exception message should indicate decoding failure");
    }

    @Test
    void testDecodeJwt_InvalidBase64Payload() {
        String invalidBase64PayloadJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                + "invalidPayload."
                + "signature";

        Exception exception = assertThrows(RuntimeException.class, () -> JwtUtils.decodeJwt(invalidBase64PayloadJwt));
        assertTrue(exception.getMessage().contains("Failed to decode JWT token."),
                "Exception message should indicate decoding failure");
    }

    @Test
    void testDecodeJwt_NullToken() {
        Exception exception = assertThrows(RuntimeException.class, () -> JwtUtils.decodeJwt(null));
        assertTrue(exception.getMessage().contains("Failed to decode JWT token."),
                "Exception message should indicate decoding failure");
    }
}