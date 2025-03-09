package pl.sankouski.boarditfront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sankouski.boarditfront.model.user.User;
import pl.sankouski.boarditfront.security.JwtUtils;
import pl.sankouski.boarditfront.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getProfile(@CookieValue(name = "jwt", required = false) String jwtToken, Model model) {
        if (jwtToken == null) {
            return "redirect:/auth/login";
        }
        userService.setToken(jwtToken);
        Map<String, Object> claims = JwtUtils.decodeJwt(jwtToken);

        String userName = (String) claims.get("sub");
        User user = userService.getUserByUsername(userName);
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRoles().get(0).getName());
        return "profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@CookieValue(name = "jwt", required = false) String jwtToken, @ModelAttribute("user") User updatedUser) {
        if (jwtToken == null) {
            return "redirect:/auth/login";
        }
        userService.setToken(jwtToken);
        Map<String, Object> claims = JwtUtils.decodeJwt(jwtToken);

        String userName = (String) claims.get("sub");
        User existingUser = userService.getUserByUsername(userName);


        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setMiddleName(updatedUser.getMiddleName());
        existingUser.setPassword(updatedUser.getPassword());

        userService.updateUser(existingUser);
        return "redirect:/profile";
    }
}