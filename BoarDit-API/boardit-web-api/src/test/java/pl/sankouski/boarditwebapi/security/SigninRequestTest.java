package pl.sankouski.boarditwebapi.security;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SigninRequestTest {

    @Test
    void shouldSetAndGetUsername() {

        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setUsername("testuser");
        assertThat(signinRequest.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldSetAndGetPassword() {
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setPassword("password123");
        assertThat(signinRequest.getPassword()).isEqualTo("password123");
    }

    @Test
    void shouldSetUsernameAndPasswordFluently() {
        // Arrange & Act
        SigninRequest signinRequest = new SigninRequest()
                .setUsername("testuser")
                .setPassword("password123");
        assertThat(signinRequest.getUsername()).isEqualTo("testuser");
        assertThat(signinRequest.getPassword()).isEqualTo("password123");
    }
}