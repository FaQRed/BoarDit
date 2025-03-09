package pl.sankouski.boarditdata.model;


import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.user.Role;
import pl.sankouski.boarditdata.model.user.Status;
import pl.sankouski.boarditdata.model.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        User user = new User();

        assertNull(user.getPid());
        assertNull(user.getLogin());
        assertNull(user.getPassword());
        assertNull(user.getFirstName());
        assertNull(user.getMiddleName());
        assertNull(user.getLastName());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
        assertNull(user.getStatus());
    }

    @Test
    public void setPid_shouldUpdatePid() {
        User user = new User();
        user.setPid(1L);

        assertEquals(1L, user.getPid());
    }

    @Test
    public void setLogin_shouldUpdateLogin() {
        User user = new User();
        user.setLogin("testUser");

        assertEquals("testUser", user.getLogin());
    }

    @Test
    public void setPassword_shouldUpdatePassword() {
        User user = new User();
        user.setPassword("securePassword");

        assertEquals("securePassword", user.getPassword());
    }

    @Test
    public void setFirstName_shouldUpdateFirstName() {
        User user = new User();
        user.setFirstName("John");

        assertEquals("John", user.getFirstName());
    }

    @Test
    public void setMiddleName_shouldUpdateMiddleName() {
        User user = new User();
        user.setMiddleName("William");

        assertEquals("William", user.getMiddleName());
    }

    @Test
    public void setLastName_shouldUpdateLastName() {
        User user = new User();
        user.setLastName("Doe");

        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void setRoles_shouldUpdateRoles() {
        User user = new User();
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_USER", "User role"));
        user.setRoles(roles);

        assertEquals(roles, user.getRoles());
    }

    @Test
    public void setStatus_shouldUpdateStatus() {
        User user = new User();
        user.setStatus(Status.ACTIVE);

        assertEquals(Status.ACTIVE, user.getStatus());
    }

    @Test
    public void getRolesStr_shouldReturnCommaSeparatedRoleNames() {
        User user = new User();
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_USER", "User role"));
        roles.add(new Role("ROLE_ADMIN", "Admin role"));
        user.setRoles(roles);

        assertEquals("ROLE_USER,ROLE_ADMIN", user.getRolesStr());
    }


}