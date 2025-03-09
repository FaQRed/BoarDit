package pl.sankouski.boarditfront.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sankouski.boarditfront.dto.*;
import pl.sankouski.boarditfront.model.user.Role;
import pl.sankouski.boarditfront.model.user.User;
import pl.sankouski.boarditfront.security.JwtUtils;
import pl.sankouski.boarditfront.service.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/boardgames")
public class BoardGameDetailsController {

    private final BoardGameService boardGameService;
    private final MechanicService mechanicService;
    private final CategoryService categoryService;
    private final ExpansionService expansionService;
    private final AlternateNameService alternateNameService;
    private final ArtistService artistService;
    private final UserService userService;

    public BoardGameDetailsController(BoardGameService boardGameService, MechanicService mechanicService,
                                      CategoryService categoryService, ExpansionService expansionService,
                                      AlternateNameService alternateNameService, ArtistService artistService,
                                      UserService userService) {
        this.boardGameService = boardGameService;
        this.mechanicService = mechanicService;
        this.categoryService = categoryService;
        this.expansionService = expansionService;
        this.alternateNameService = alternateNameService;
        this.artistService = artistService;
        this.userService = userService;
    }

    @GetMapping("/{id}/details")
    public String getBoardGameDetails(@CookieValue(value = "jwt", required = false) String token, @PathVariable Long id, Model model) {
        if (token == null) {
            return "redirect:/auth/login";
        }
        if (token != null) {
            userService.setToken(token);
            Map<String, Object> claims = JwtUtils.decodeJwt(token);

            String userName = (String) claims.get("sub");

            User user = userService.getUserByUsername(userName);
            model.addAttribute("user", user);
            Role role = user.getRoles().get(0);

            model.addAttribute("role", role.getName());
        } else {
            model.addAttribute("role", null);
        }

        BoardGameDTO boardGame = boardGameService.getBoardGameById(id);

        boardGameService.setToken(token);
        mechanicService.setToken(token);
        categoryService.setToken(token);
        expansionService.setToken(token);
        alternateNameService.setToken(token);
        artistService.setToken(token);


        List<MechanicDTO> mechanics = mechanicService.getMechanicsByIds(boardGame.getMechanics());
        List<CategoryDTO> categories = categoryService.getCategoriesByIds(boardGame.getCategories());
        List<ExpansionDTO> expansions = expansionService.getExpansionsByBoardGameId(id);
        List<AlternateNameDTO> alternateNames = alternateNameService.getAllAlternateNamesByGameId(id);
        List<ArtistDTO> artists = artistService.getArtistsByNames(boardGame.getArtists());


        model.addAttribute("boardGame", boardGame);
        model.addAttribute("mechanics", mechanics);
        model.addAttribute("categories", categories);
        model.addAttribute("expansions", expansions);
        model.addAttribute("alternateNames", alternateNames);
        model.addAttribute("artists", artists);

        return "game-details";

    }
}