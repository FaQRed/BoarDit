package pl.sankouski.boarditfront.controller.admin_panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sankouski.boarditfront.model.user.User;
import pl.sankouski.boarditfront.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String usersPage(@CookieValue("jwt") String token,
                            @RequestParam(value = "filterText", required = false) String filterText,
                            Model model) {
        userService.setToken(token);
        try {
            List<User> users = (filterText == null || filterText.isEmpty())
                    ? userService.getAllUsers()
                    : userService.filterUser(filterText);
            model.addAttribute("users", users);
            model.addAttribute("filterText", filterText);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("pageTitle", "Users Management");
        model.addAttribute("activePage", "users");
        model.addAttribute("roles", userService.getAllRoles());
        return "admin/users";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, Model model) {
        try {
            userService.saveUser(user);
            model.addAttribute("success", "User successfully added!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", userService.getAllRoles());
        return "admin/users";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, @CookieValue("jwt") String token, Model model) {
        try {
            userService.setToken(token);
            userService.updateUser(user);
            model.addAttribute("success", "User successfully updated!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", userService.getAllRoles());
        return "admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, @CookieValue("jwt") String token, Model model) {
        try {
            userService.setToken(token);
            User currentUser = userService.getCurrentUserFromToken(token);

            if (currentUser.getPid().equals(id)) {
                model.addAttribute("error", "You cannot delete your own account.");
            } else {
                userService.deleteUserById(id);
                model.addAttribute("success", "User successfully deleted.");
            }
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", userService.getAllRoles());
        return "admin/users";
    }
}