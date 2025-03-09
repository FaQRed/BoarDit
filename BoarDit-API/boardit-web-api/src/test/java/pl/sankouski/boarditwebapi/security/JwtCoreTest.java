package pl.sankouski.boarditwebapi.security;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtCoreTest {

    private JwtCore jwtCore;

    @BeforeEach
    void setUp() {
        String testSecret = "superSecretForJwt1231231231232342342";
        long testExpirationTime = 3600000;
        jwtCore = new JwtCore(testSecret, testExpirationTime);
    }

    @Test
    void generateToken_shouldGenerateValidJwt() {
        String username = "testUser";
        var authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtCore.generateToken(authentication);

        assertNotNull(token, "Token must not be null");

        Claims claims = jwtCore.parseToken(token);
        assertEquals(username, claims.getSubject(), "Subject in token should match the username");
        assertEquals("ROLE_USER", claims.get("roles"), "Roles should match the provided authority");
    }

    @Test
    void parseToken_shouldReturnCorrectClaims() {
        String username = "testUser";
        var authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        String token = jwtCore.generateToken(authentication);

        Claims claims = jwtCore.parseToken(token);

        assertNotNull(claims, "Claims must not be null");
        assertEquals(username, claims.getSubject(), "Subject in token should match the username");
        assertEquals("ROLE_ADMIN", claims.get("roles"), "Roles should match the provided authority");
    }

    @Test
    void parseToken_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> jwtCore.parseToken(invalidToken), "Invalid token should throw an exception");
    }
}
