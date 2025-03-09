package pl.sankouski.boarditupdater;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.sankouski.boarditupdater.boardGames.updater.IUpdateBoardGames;


@SpringBootApplication(scanBasePackages = "pl.sankouski.*")
public class BoarditUpdaterApplication implements CommandLineRunner {
private final IUpdateBoardGames updater;

    public BoarditUpdaterApplication(IUpdateBoardGames updater) {
        this.updater = updater;
    }

    public static void main(String[] args) {
        SpringApplication.run(BoarditUpdaterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//     updater.updateByNumOfPeaces(10);
    }
}
