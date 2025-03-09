package pl.sankouski.boarditdata.model.boardgame;
import jakarta.persistence.*;

@Entity
public class AlternateName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "board_game_id")
    private BoardGame boardGame;

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public AlternateName setName(String name) {
        this.name = name;
        return this;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public AlternateName setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
        return this;
    }

    public AlternateName setId(Long id) {
        this.id = id;
        return this;
    }
}
