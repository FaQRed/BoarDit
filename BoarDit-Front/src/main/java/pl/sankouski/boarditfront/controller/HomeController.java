package pl.sankouski.boarditfront.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;


import pl.sankouski.boarditfront.dto.BoardGameSummaryDto;
import pl.sankouski.boarditfront.model.user.Role;
import pl.sankouski.boarditfront.model.user.User;
import pl.sankouski.boarditfront.security.JwtUtils;
import pl.sankouski.boarditfront.service.BoardGameService;
import pl.sankouski.boarditfront.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private UserService userService;
    private BoardGameService boardGameService;

    public HomeController(UserService userService, BoardGameService boardGameService) {
        this.userService = userService;
        this.boardGameService = boardGameService;
    }

    @GetMapping("/")
    public String homePage(@CookieValue(name = "jwt", required = false) String jwtToken, Model model) {
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
        try {
            boardGameService.setToken(jwtToken);
            List<BoardGameSummaryDto> boardGames = boardGameService.getAllBoardGamesSummary();
            model.addAttribute("boardGames", boardGames);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load board games: " + e.getMessage());
        }

        return "index";
    }
}