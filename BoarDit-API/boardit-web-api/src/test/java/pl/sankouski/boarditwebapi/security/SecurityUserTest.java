package pl.sankouski.boarditwebapi.security;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.user.Role;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sankouski.boarditdata.model.user.Status;
import pl.sankouski.boarditdata.model.user.User;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

class SecurityUserTest {

    @Test
    void shouldCreateSecurityUserFromUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password123");
        user.setStatus(Status.ACTIVE);
        user.setRoles(List.of(new Role("ROLE_USER", "User Role")));

        UserDetails userDetails = SecurityUser.fromUser(user);

        assertThat(userDetails.getUsername()).isEqualTo("testuser");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void shouldReturnInactiveUserDetails() {
        User user = new User();
        user.setLogin("inactiveUser");
        user.setPassword("password456");
        user.setStatus(Status.BANE);
        user.setRoles(List.of(new Role("ROLE_ADMIN", "Admin Role")));

        UserDetails userDetails = SecurityUser.fromUser(user);

        assertThat(userDetails.getUsername()).isEqualTo("inactiveUser");
        assertThat(userDetails.getPassword()).isEqualTo("password456");
        assertThat(userDetails.isAccountNonExpired()).isFalse();
        assertThat(userDetails.isAccountNonLocked()).isFalse();
        assertThat(userDetails.isCredentialsNonExpired()).isFalse();
        assertThat(userDetails.isEnabled()).isFalse();
    }

}