package pl.sankouski.boarditwebapi.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sankouski.boarditboardgamesclient.exception.UserAlreadyExistsException;
import pl.sankouski.boarditboardgamesclient.exception.UserNotFoundException;
import pl.sankouski.boarditdata.model.user.Role;
import pl.sankouski.boarditdata.model.user.Status;
import pl.sankouski.boarditdata.model.user.User;

import pl.sankouski.boarditdata.repository.UserRepository;
import pl.sankouski.boarditwebapi.registration.UserRegistrationDto;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return userRepository.getAllRolesForUser();
    }

    @Override
    @Transactional
    public User createNewUser(User user) {
        if (!isLoginUnique(user.getLogin())) {
            throw new UserAlreadyExistsException("User with this login is already exist");
        }
        if (user.getRoles().isEmpty()) {
            user.setRoles(List.of(new Role("ROLE_USER", "user")));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long pid) {
        return userRepository.findById(pid);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getPid())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!existingUser.getLogin().equals(user.getLogin()) && !isLoginUnique(user.getLogin())) {
            throw new UserAlreadyExistsException("User with this login already exists");
        }

        if (!Objects.equals(user.getPassword(), "")) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (!user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }

        existingUser.setFirstName(user.getFirstName());
        existingUser.setMiddleName(user.getMiddleName());
        existingUser.setLastName(user.getLastName());

        existingUser.setStatus(user.getStatus());
        existingUser.setLogin(user.getLogin());

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long pid) {
        userRepository.deleteById(pid);
    }


    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public List<User> filterUser(String filterText) {
        return userRepository.filterUser(filterText);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public boolean isLoginUnique(String login) {
        return userRepository.findByLogin(login).isEmpty();
    }

    public User save(UserRegistrationDto registration) {

        User user = new User();
        user.setLogin(registration.getLogin());
        if (!isLoginUnique(user.getLogin())) {
            throw new UserAlreadyExistsException("User with this login is already exist");
        }
        user.setFirstName(registration.getFirstName());
        user.setMiddleName(registration.getMiddleName());
        user.setLastName(registration.getLastName());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(List.of(new Role("ROLE_USER", "")));
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
    }
}
