package pl.sankouski.boarditfront.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sankouski.boarditfront.dto.MechanicDTO;
import pl.sankouski.boarditfront.service.MechanicService;

import java.util.List;

@Controller
@RequestMapping("/admin/mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public String listMechanics(@CookieValue("jwt") String token, Model model) {
        mechanicService.setToken(token);
        List<MechanicDTO> mechanics = mechanicService.getAllMechanics();
        model.addAttribute("mechanics", mechanics);
        return "admin/mechanics";
    }

    @PostMapping("/add")
    public String addMechanic(@RequestParam String name, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        mechanicService.setToken(token);

        try {
            MechanicDTO mechanicDTO = new MechanicDTO();
            mechanicDTO.setName(name);
            mechanicService.createMechanic(mechanicDTO);
            redirectAttributes.addFlashAttribute("success", "Mechanic successfully added!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/mechanics";
    }

    @GetMapping("/delete/{id}")
    public String deleteMechanic(@PathVariable Long id, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        mechanicService.setToken(token);

        try {
            mechanicService.deleteMechanic(id);
            redirectAttributes.addFlashAttribute("success", "Mechanic successfully deleted!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/mechanics";
    }

    @PostMapping("/edit/{id}")
    public String updateMechanic(@PathVariable Long id, @RequestParam String name, @CookieValue("jwt") String token, RedirectAttributes redirectAttributes) {
        mechanicService.setToken(token);

        try {
            MechanicDTO mechanicDTO = new MechanicDTO();
            mechanicDTO.setId(id);
            mechanicDTO.setName(name);
            mechanicService.updateMechanic(id, mechanicDTO);
            redirectAttributes.addFlashAttribute("success", "Mechanic successfully updated!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/mechanics";
    }
}