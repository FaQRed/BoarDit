package pl.sankouski.boarditdata.model;

import org.junit.jupiter.api.Test;
import pl.sankouski.boarditdata.model.user.Role;
import pl.sankouski.boarditdata.model.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void defaultConstructor_shouldInitializeWithDefaultValues() {
        Role role = new Role();

        assertNull(role.getPid());
        assertNull(role.getDescription());
        assertNotNull(role.getUsers());
        assertTrue(role.getUsers().isEmpty());
    }

    @Test
    public void parameterizedConstructor_shouldInitializeFields() {
        Role role = new Role("ROLE_ADMIN", "Administrator role");

        assertEquals("ROLE_ADMIN", role.getPid());
        assertEquals("Administrator role", role.getDescription());
    }

    @Test
    public void setPid_shouldUpdatePid() {
        Role role = new Role();
        role.setPid("ROLE_USER");

        assertEquals("ROLE_USER", role.getPid());
    }

    @Test
    public void setDescription_shouldUpdateDescription() {
        Role role = new Role();
        role.setDescription("User role");

        assertEquals("User role", role.getDescription());
    }

    @Test
    public void getName_shouldReturnPid() {
        Role role = new Role("ROLE_MODERATOR", "Moderator role");

        assertEquals("ROLE_MODERATOR", role.getName());
    }

    @Test
    public void getAuthority_shouldReturnPid() {
        Role role = new Role("ROLE_VIEWER", "Viewer role");

        assertEquals("ROLE_VIEWER", role.getAuthority());
    }

    @Test
    public void usersList_shouldBeMutable() {
        Role role = new Role();
        List<User> users = new ArrayList<>();
        users.add(new User());
        role.getUsers().addAll(users);

        assertEquals(1, role.getUsers().size());
        assertSame(users.get(0), role.getUsers().get(0));
    }

    @Test
    public void usersList_shouldBeEmptyByDefault() {
        Role role = new Role();

        assertNotNull(role.getUsers());
        assertTrue(role.getUsers().isEmpty());
    }
}