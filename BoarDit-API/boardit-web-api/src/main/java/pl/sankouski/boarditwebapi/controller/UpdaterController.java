package pl.sankouski.boarditwebapi.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.sankouski.boarditdata.repository.BoarDitDataCatalog;
import pl.sankouski.boarditupdater.boardGames.updater.IUpdateBoardGames;

@Controller
@RequestMapping("/admin")
public class UpdaterController {

    final IUpdateBoardGames updater;
    final BoarDitDataCatalog catalog;

    public UpdaterController(IUpdateBoardGames updater, BoarDitDataCatalog catalog) {
        this.updater = updater;
        this.catalog = catalog;
    }

    @GetMapping("/updater/start")
    public ResponseEntity start(@RequestParam int number) {
        updater.updateByIdFromBgg(number);
        return catalog.getBoardGame().findByBggId((long) number)
                .map(boardGame -> ResponseEntity.ok("Was added/updated " + boardGame.getName() + " game"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No game found with BGG ID: " + number));
    }
}
