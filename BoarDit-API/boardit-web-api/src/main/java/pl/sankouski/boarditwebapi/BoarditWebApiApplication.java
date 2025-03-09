package pl.sankouski.boarditwebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "pl.sankouski.boarditdata.repository")
@SpringBootApplication(scanBasePackages = "pl.sankouski.*")
public class BoarditWebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoarditWebApiApplication.class, args);
    }

}
