package pl.sankouski.boarditwebapi.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.sankouski.boarditdata.model.user.Role;
import pl.sankouski.boarditdata.model.user.User;
import pl.sankouski.boarditwebapi.registration.UserRegistrationDto;

import java.util.List;
import java.util.Optional;

public interface IUserService  extends UserDetailsService {

    List<User> getAllUsers();

    List<Role> getAllRoles();

    User createNewUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long pid);

    Optional<User> getUserByLogin(String login);

    void deleteUserById(Long pid);

    List<User> filterUser(String filterText);

     User save(UserRegistrationDto registration);

     User getUserByUsername(String username);
}
