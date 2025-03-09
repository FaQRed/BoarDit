package pl.sankouski.boarditwebapi.security;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.sankouski.boarditdata.model.user.Role;
import pl.sankouski.boarditdata.model.user.Status;
import pl.sankouski.boarditdata.model.user.User;
import pl.sankouski.boarditdata.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        String username = "testuser";
        User user = new User();
        user.setLogin(username);
        user.setPassword("password123");
        user.setStatus(Status.ACTIVE);
        user.setRoles(List.of(new Role("ROLE_USER", "Default user role")));

        when(userRepository.findByLogin(username)).thenReturn(Optional.of(user));

        var userDetails = userDetailsService.loadUserByUsername(username);

        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.getAuthorities()).hasSize(1)
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));

        verify(userRepository, times(1)).findByLogin(username);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String username = "nonexistent";

        when(userRepository.findByLogin(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User doesn't exists");

        verify(userRepository, times(1)).findByLogin(username);
    }
}