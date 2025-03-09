package pl.sankouski.boarditfront.controller.admin_panel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sankouski.boarditfront.service.UpdaterService;

@Controller
@RequestMapping("/admin/updater")
public class UpdaterController {

    private final UpdaterService updaterService;

    public UpdaterController(UpdaterService updaterService) {
        this.updaterService = updaterService;
    }

    @RequestMapping
    public String updaterPage(Model model) {
        model.addAttribute("pageTitle", "Update Board Game");
        return "admin/updater";
    }

    @PostMapping("/start")
    public String startUpdate(@RequestParam("number") int number,
                              @CookieValue(value = "jwt", required = false) String jwtToken,
                              Model model) {
        try {
            if (jwtToken != null) {
                updaterService.setToken(jwtToken);
            }

            String result = updaterService.updateBoardGame(number);
            model.addAttribute("result", result);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/updater";
    }
}