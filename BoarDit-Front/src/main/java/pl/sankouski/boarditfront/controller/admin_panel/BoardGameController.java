package pl.sankouski.boarditfront.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sankouski.boarditfront.dto.*;
import pl.sankouski.boarditfront.service.*;

import java.util.List;

@Controller
@RequestMapping("/admin/boardgames")
public class BoardGameController {

    private final BoardGameService boardGameService;
    private final AlternateNameService alternateNameService;
    private final ArtistService artistService;
    private final CategoryService categoryService;
    private final MechanicService mechanicService;

    public BoardGameController(BoardGameService boardGameService, AlternateNameService alternateNameService, ArtistService artistService, CategoryService categoryService, MechanicService mechanicService) {
        this.boardGameService = boardGameService;
        this.alternateNameService = alternateNameService;
        this.artistService = artistService;
        this.categoryService = categoryService;
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public String listBoardGames(@CookieValue("jwt") String token, Model model) {
        boardGameService.setToken(token);
        model.addAttribute("pageTitle", "Board Games Management");
        model.addAttribute("activePage", "boardgames");
        model.addAttribute("boardGames", boardGameService.getAllBoardGames());
        return "admin/boardgames";
    }

    @GetMapping("/{id}")
    public String getBoardGame(@PathVariable Long id, @CookieValue("jwt") String token, Model model) {
        boardGameService.setToken(token);
        alternateNameService.setToken(token);
        artistService.setToken(token);
        categoryService.setToken(token);
        mechanicService.setToken(token);

        BoardGameDTO boardGame = boardGameService.getBoardGameById(id);
        List<CategoryDTO> categories = categoryService.getCategoriesByIds(boardGame.getCategories());
        List<MechanicDTO> mechanics = mechanicService.getMechanicsByIds(boardGame.getMechanics());
        List<AlternateNameDTO> alternateNameDTOS = alternateNameService.getAllAlternateNamesByGameId(id);
        List<ArtistDTO> artistDTOS = artistService.getArtistsByNames(boardGame.getArtists());
        List<CategoryDTO> availableCategories = categoryService.getAllCategories();
        List<MechanicDTO> availableMechanics = mechanicService.getAllMechanics();
        model.addAttribute("boardGame", boardGame);
        model.addAttribute("alternateNames", alternateNameDTOS);
        model.addAttribute("categories", categories);
        model.addAttribute("availableCategories", availableCategories);
        model.addAttribute("mechanics", mechanics);
        model.addAttribute("availableMechanics", availableMechanics);
        model.addAttribute("artists", artistDTOS);


        return "admin/boardgame-details";
    }

    @GetMapping("/add")
    public String addBoardGamePage(@CookieValue("jwt") String token, Model model) {
        boardGameService.setToken(token);
        categoryService.setToken(token);
        mechanicService.setToken(token);

        List<CategoryDTO> availableCategories = categoryService.getAllCategories();
        List<MechanicDTO> availableMechanics = mechanicService.getAllMechanics();

        model.addAttribute("availableCategories", availableCategories);
        model.addAttribute("availableMechanics", availableMechanics);
        model.addAttribute("boardGame", new BoardGameDTO());

        return "admin/add-board-game";
    }


    @PostMapping("/add")
    public String addBoardGame(@ModelAttribute BoardGameDTO boardGameDTO,
                               @CookieValue("jwt") String token,
                               RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);

        try {
            boardGameService.createBoardGame(boardGameDTO);
            redirectAttributes.addFlashAttribute("success", "Board game successfully added!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/boardgames/add";
        }

        return "redirect:/admin/boardgames";
    }


    @GetMapping("/delete/{id}")
    public String deleteBoardGame(@PathVariable Long id, @CookieValue("jwt") String token, Model model) {
        boardGameService.setToken(token);
        try {
            boardGameService.deleteBoardGame(id);
            model.addAttribute("success", "Board game successfully deleted!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("boardGames", boardGameService.getAllBoardGames());
        return "admin/boardgames";
    }

    @PostMapping("/{id}/add-artist")
    public String addArtist(@PathVariable Long id, @RequestParam String artistName, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            ArtistDTO dto = new ArtistDTO();
            dto.setName(artistName);
            artistService.createArtistForBoardGame(id, dto);
            redirectAttributes.addFlashAttribute("success", "Artist successfully added!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + id;
    }

    @GetMapping("/{id}/delete-artist")
    public String deleteArtist(@PathVariable Long id, @RequestParam Long artistId, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            artistService.deleteArtist(artistId);
            redirectAttributes.addFlashAttribute("success", "Artist successfully deleted!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + id;
    }

    @PostMapping("/{id}/add-alternate-name")
    public String addAlternateName(@PathVariable Long id, @RequestParam String altName, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            AlternateNameDTO dto = new AlternateNameDTO();
            dto.setName(altName);
            alternateNameService.createAlternateName(id, dto);
            redirectAttributes.addFlashAttribute("success", "Alternate name successfully added!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + id;
    }

    @GetMapping("/{id}/delete-alternate-name")
    public String deleteAlternateName(@PathVariable Long id, @RequestParam Long altNameId, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            alternateNameService.deleteAlternateName(id, altNameId);
            redirectAttributes.addFlashAttribute("success", "Alternate name successfully deleted!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + id;
    }

    @PostMapping("/{boardGameId}/add-category")
    public String addCategoryToBoardGame(@PathVariable Long boardGameId, @RequestParam Long categoryId, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            boardGameService.addCategoryToBoardGame(boardGameId, categoryId);
            redirectAttributes.addFlashAttribute("success", "Category successfully added!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + boardGameId;
    }

    @GetMapping("/{boardGameId}/remove-category")
    public String removeCategoryFromBoardGame(@PathVariable Long boardGameId, @RequestParam Long categoryId, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            boardGameService.removeCategoryFromBoardGame(boardGameId, categoryId);
            redirectAttributes.addFlashAttribute("success", "Category successfully removed!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + boardGameId;
    }

    @PostMapping("/{boardGameId}/add-mechanic")
    public String addMechanicToBoardGame(@PathVariable Long boardGameId, @RequestParam Long mechanicId, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            boardGameService.addMechanicToBoardGame(boardGameId, mechanicId);
            redirectAttributes.addFlashAttribute("success", "Mechanic successfully added!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + boardGameId;
    }



    @GetMapping("/{boardGameId}/remove-mechanic")
    public String removeMechanicFromBoardGame(@PathVariable Long boardGameId, @RequestParam Long mechanicId, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);
        try {
            boardGameService.removeMechanicFromBoardGame(boardGameId, mechanicId);
            redirectAttributes.addFlashAttribute("success", "Mechanic successfully removed!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + boardGameId;
    }


    @GetMapping("/edit/{id}")
    public String editBoardGamePage(@PathVariable Long id, @CookieValue("jwt") String token, Model model) {
        boardGameService.setToken(token);

        try {
            BoardGameDTO boardGame = boardGameService.getBoardGameById(id);
            model.addAttribute("boardGame", boardGame);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/boardgames";
        }

        return "admin/edit-boardgame";
    }

    @PostMapping("/edit/{id}")
    public String updateBoardGameDetails(@PathVariable Long id, @ModelAttribute BoardGameUpdateSummaryDTO boardGameDTO,
                                         @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        boardGameService.setToken(token);

        try {
            BoardGameDTO currentDTO = boardGameService.getBoardGameById(id);
            boardGameService.updateBoardGame(id, boardGameDTO);
            redirectAttributes.addFlashAttribute("success", "Board game details successfully updated!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/boardgames/" + id;
    }

    @GetMapping("/{boardGameId}/edit-artist/{artistId}")
    public String editArtistPage(@PathVariable Long boardGameId, @PathVariable Long artistId, @CookieValue("jwt") String token, Model model) {
        artistService.setToken(token);

        try {
            ArtistDTO artist = artistService.getArtistById(artistId);
            model.addAttribute("artist", artist);
            model.addAttribute("boardGameId", boardGameId);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/boardgames/" + boardGameId;
        }

        return "admin/edit-artist";
    }

    @PostMapping("/{boardGameId}/edit-artist/{artistId}")
    public String updateArtistName(@PathVariable Long boardGameId, @PathVariable Long artistId,
                                   @RequestParam String name, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        artistService.setToken(token);

        try {
            ArtistDTO artistDTO = new ArtistDTO();
            artistDTO.setId(artistId);
            artistDTO.setName(name);
            artistService.updateArtist(artistId, artistDTO);
            redirectAttributes.addFlashAttribute("success", "Artist name successfully updated!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/boardgames/" + boardGameId;
    }

    @GetMapping("/{boardGameId}/edit-alternate-name/{altNameId}")
    public String editAlternateNamePage(@PathVariable Long boardGameId, @PathVariable Long altNameId, @CookieValue("jwt") String token, Model model) {
        alternateNameService.setToken(token);

        try {
            AlternateNameDTO altName = alternateNameService.getAlternateNameById(boardGameId, altNameId);
            model.addAttribute("altName", altName);
            model.addAttribute("boardGameId", boardGameId);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/boardgames/" + boardGameId;
        }

        return "admin/edit-alternate-name";
    }

    @PostMapping("/{boardGameId}/edit-alternate-name/{altNameId}")
    public String updateAlternateName(@PathVariable Long boardGameId, @PathVariable Long altNameId,
                                      @RequestParam String name, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        alternateNameService.setToken(token);

        try {
            AlternateNameDTO altNameDTO = new AlternateNameDTO();
            altNameDTO.setId(altNameId);
            altNameDTO.setName(name);
            alternateNameService.updateAlternateName(boardGameId, altNameId, altNameDTO);
            redirectAttributes.addFlashAttribute("success", "Alternate name successfully updated!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/boardgames/" + boardGameId;
    }

}