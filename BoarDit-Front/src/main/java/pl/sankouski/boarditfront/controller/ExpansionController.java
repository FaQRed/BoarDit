package pl.sankouski.boarditfront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sankouski.boarditfront.dto.ExpansionDTO;
import pl.sankouski.boarditfront.model.user.Role;
import pl.sankouski.boarditfront.model.user.User;
import pl.sankouski.boarditfront.security.JwtUtils;
import pl.sankouski.boarditfront.service.ExpansionService;
import pl.sankouski.boarditfront.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/expansion")
public class ExpansionController {

    private final ExpansionService expansionService;
    private final UserService userService;

    public ExpansionController(ExpansionService expansionService, UserService userService) {
        this.expansionService = expansionService;
        this.userService = userService;
    }

    @GetMapping("/{id}/details")
    public String getExpansionDetails(@CookieValue("jwt") String jwtToken, @PathVariable Long id, Model model) {
        if (jwtToken == null) {
            return "redirect:/auth/login";
        }
        if (jwtToken != null) {
            userService.setToken(jwtToken);
            Map<String, Object> claims = JwtUtils.decodeJwt(jwtToken);

            String userName = (String) claims.get("sub");

            User user = userService.getUserByUsername(userName);
            model.addAttribute("user", user);
            Role role = user.getRoles().get(0);

            model.addAttribute("role", role.getName());
        } else {
            model.addAttribute("role", null);
        }
            expansionService.setToken(jwtToken);
            ExpansionDTO expansion = expansionService.getExpansionById(id);
            model.addAttribute("expansion", expansion);
            return "expansion-details";

    }
}