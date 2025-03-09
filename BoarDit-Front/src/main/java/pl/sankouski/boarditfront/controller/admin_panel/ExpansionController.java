package pl.sankouski.boarditfront.controller.admin_panel;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sankouski.boarditfront.dto.ExpansionDTO;
import pl.sankouski.boarditfront.service.ExpansionService;

@Controller("adminExpansionController")
@RequestMapping("/admin/boardgames/{boardGameId}/expansions")
public class ExpansionController {

    private final ExpansionService expansionService;

    public ExpansionController(ExpansionService expansionService) {
        this.expansionService = expansionService;
    }

    @GetMapping
    public String listExpansions(@PathVariable Long boardGameId, @CookieValue(value = "jwt", required = false) String token, Model model) {
        expansionService.setToken(token);
        model.addAttribute("expansions", expansionService.getExpansionsByBoardGameId(boardGameId));
        model.addAttribute("boardGameId", boardGameId);
        return "admin/expansions";
    }

    @PostMapping("/add")
    public String addExpansion(@PathVariable Long boardGameId,
                               @CookieValue(value = "jwt", required = false) String token,
                               @ModelAttribute ExpansionDTO expansionDTO,
                               RedirectAttributes redirectAttributes) {
        try {
            expansionService.setToken(token);
            expansionService.createExpansion(boardGameId, expansionDTO);
            redirectAttributes.addFlashAttribute("success", "Expansion added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + boardGameId + "/expansions";
    }

    @PostMapping("/edit/{id}")
    public String editExpansion(@PathVariable Long boardGameId,
                                @PathVariable Long id,
                                @CookieValue(value = "jwt", required = false) String token,
                                @ModelAttribute ExpansionDTO expansionDTO,
                                RedirectAttributes redirectAttributes) {
        try {
            expansionService.setToken(token);
            expansionDTO.setId(id);
            expansionService.updateExpansion(boardGameId,expansionDTO.getId(), expansionDTO);
            redirectAttributes.addFlashAttribute("success", "Expansion updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + boardGameId + "/expansions";
    }

    @GetMapping("/edit/{id}")
    public String showEditExpansionPage(@PathVariable Long boardGameId,
                                        @PathVariable Long id,
                                        @CookieValue(value = "jwt", required = false) String token,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        expansionService.setToken(token);
        try {
            ExpansionDTO expansionDTO = expansionService.getExpansionById(id);
            model.addAttribute("expansionDTO", expansionDTO);
            model.addAttribute("boardGameId", boardGameId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to load expansion details: " + e.getMessage());
            return "redirect:/admin/boardgames/" + boardGameId + "/expansions";
        }
        return "admin/edit-expansion";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpansion(@PathVariable Long boardGameId,
                                  @PathVariable Long id,
                                  @CookieValue(value = "jwt", required = false) String token,
                                  RedirectAttributes redirectAttributes) {
        try {
            expansionService.setToken(token);
            expansionService.deleteExpansion(id);
            redirectAttributes.addFlashAttribute("success", "Expansion deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/boardgames/" + boardGameId + "/expansions";
    }
}