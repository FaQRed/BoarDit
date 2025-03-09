package pl.sankouski.boarditfront.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.sankouski.boarditfront.security.LoginRequest;

@Controller
@RequestMapping("/auth")
public class AuthController {


    private final RestTemplate restTemplate;


    @Value("${api.url}")
    private String apiUrl;


    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping("/login")
    private String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response,
            Model model
    ) {

        var loginRequest = new LoginRequest(username, password);

        try {

            ResponseEntity<String> apiResponse = restTemplate.postForEntity(
                    apiUrl + "/auth/signin",
                    loginRequest,
                    String.class
            );


            String token = apiResponse.getBody();


            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(jwtCookie);

            return "redirect:/";
        } catch (Exception e) {

            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());


        return "redirect:/";
    }

}