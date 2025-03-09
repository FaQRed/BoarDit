package pl.sankouski.boarditfront.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sankouski.boarditfront.security.JwtUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {



    @GetMapping
    public String adminDashboard(@CookieValue(value = "jwt", required = false) String jwtToken, Model model) {
        if (jwtToken == null) {
            return "redirect:/auth/login";
        }
        Map<String, Object> claims = JwtUtils.decodeJwt(jwtToken);
        Object rolesObject = claims.get("roles");
        List<String> roles;
        roles = Arrays.asList(((String) rolesObject).split(","));


        if (!roles.contains("ROLE_ADMIN")) {
            return "redirect:/access-denied";
        }


        model.addAttribute("pageTitle", "Admin Panel");
        model.addAttribute("activePage", "dashboard");
        return "admin/index";
    }
}

