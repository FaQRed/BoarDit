package pl.sankouski.boarditwebapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sankouski.boarditboardgamesclient.exception.UserAlreadyExistsException;
import pl.sankouski.boarditboardgamesclient.exception.UserNotFoundException;
import pl.sankouski.boarditdata.model.user.Role;
import pl.sankouski.boarditdata.model.user.Status;
import pl.sankouski.boarditdata.model.user.User;
import pl.sankouski.boarditdata.repository.UserRepository;
import pl.sankouski.boarditwebapi.registration.UserRegistrationDto;
import pl.sankouski.boarditwebapi.service.user.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldGetUserById() {
        User user = new User();
        user.setPid(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getPid());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCreateNewUser() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("password");
        user.setRoles(List.of());

        when(userRepository.findByLogin("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createNewUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getLogin());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(1, result.getRoles().size());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldNotCreateUserIfLoginExists() {
        User user = new User();
        user.setLogin("existinguser");

        when(userRepository.findByLogin("existinguser")).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createNewUser(user));
    }

    @Test
    void shouldUpdateUser() {
        User existingUser = new User();
        existingUser.setPid(1L);
        existingUser.setLogin("olduser");
        existingUser.setPassword("oldpassword");
        existingUser.setRoles(List.of(new Role("ROLE_USER", "")));

        User updatedUser = new User();
        updatedUser.setPid(1L);
        updatedUser.setLogin("newuser");
        updatedUser.setPassword("newpassword");
        updatedUser.setRoles(List.of(new Role("ROLE_ADMIN", "")));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("newuser", result.getLogin());
        assertEquals("encodedNewPassword", result.getPassword());
        assertEquals(1, result.getRoles().size());
        assertEquals("ROLE_ADMIN", result.getRoles().get(0).getName());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void shouldThrowIfUserNotFoundOnUpdate() {
        User user = new User();
        user.setPid(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void shouldDeleteUserById() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldSaveUserFromRegistrationDto() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setLogin("newuser");
        registrationDto.setPassword("password");
        registrationDto.setConfirmPassword("password");
        registrationDto.setFirstName("Test");
        registrationDto.setLastName("User");

        when(userRepository.findByLogin("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.save(registrationDto);

        assertNotNull(result);
        assertEquals("newuser", result.getLogin());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowIfSavingUserWithDuplicateLogin() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setLogin("existinguser");

        when(userRepository.findByLogin("existinguser")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.save(registrationDto));
    }

    @Test
    void shouldThrowExceptionIfUserNotFoundOnDelete() {
        doThrow(new UserNotFoundException("User not found")).when(userRepository).deleteById(999L);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(999L));

        verify(userRepository, times(1)).deleteById(999L);
    }

    @Test
    void shouldLoadUserByUsername() {
        User user = new User();
        user.setLogin("testuser");
        user.setPassword("encodedPassword");
        user.setRoles(List.of(new Role("ROLE_USER", "User Role")));

        when(userRepository.findUserByLogin("testuser")).thenReturn(user);

        UserDetails result = userService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void shouldThrowExceptionIfUsernameNotFound() {
        when(userRepository.findUserByLogin("nonexistent")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("nonexistent")
        );

        assertEquals("Invalid username or password.", exception.getMessage());
        verify(userRepository, times(1)).findUserByLogin("nonexistent");
    }

    @Test
    void shouldFilterUsersByFilterText() {
        User user1 = new User();
        user1.setLogin("testuser1");

        User user2 = new User();
        user2.setLogin("testuser2");

        when(userRepository.filterUser("test")).thenReturn(List.of(user1, user2));

        List<User> result = userService.filterUser("test");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testuser1", result.get(0).getLogin());
        assertEquals("testuser2", result.get(1).getLogin());
        verify(userRepository, times(1)).filterUser("test");
    }

    @Test
    void shouldReturnEmptyListWhenFilterUserNotFound() {
        when(userRepository.filterUser("nonexistent")).thenReturn(List.of());

        List<User> result = userService.filterUser("nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).filterUser("nonexistent");
    }

    @Test
    void shouldMapRolesToAuthoritiesUsingReflection() throws Exception {
        Role role1 = new Role("ROLE_USER", "User Role");
        Role role2 = new Role("ROLE_ADMIN", "Admin Role");

        var method = UserService.class.getDeclaredMethod("mapRolesToAuthorities", Collection.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Collection<? extends GrantedAuthority> result =
                (Collection<? extends GrantedAuthority>) method.invoke(userService, List.of(role1, role2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingUserWithDuplicateLogin() {
        User existingUser = new User();
        existingUser.setPid(1L);
        existingUser.setLogin("existinguser");

        User updatedUser = new User();
        updatedUser.setPid(1L);
        updatedUser.setLogin("duplicateuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByLogin("duplicateuser")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(updatedUser));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByLogin("duplicateuser");
    }
}