package pl.sankouski.boarditdata.model.boardgame;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Mechanic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bggId;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "mechanics")
    private List<BoardGame> boardGames;

    public Mechanic() {
    }

    public Mechanic(Long id, Long bggId, String name, List<BoardGame> boardGames) {
        this.id = id;
        this.bggId = bggId;
        this.name = name;
        this.boardGames = boardGames;
    }

    public Mechanic(Long id, String name, List<BoardGame> boardGames) {
        this.id = id;
        this.name = name;
        this.boardGames = boardGames;
    }

    public Long getId() {
        return id;
    }

    public Mechanic setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBggId() {
        return bggId;
    }

    public Mechanic setBggId(Long bggId) {
        this.bggId = bggId;
        return this;
    }
    public String getName() {
        return name;
    }

    public Mechanic setName(String name) {
        this.name = name;
        return this;
    }

    public List<BoardGame> getBoardGames() {
        return boardGames;
    }

    public Mechanic setBoardGames(List<BoardGame> boardGames) {
        this.boardGames = boardGames;
        return this;
    }
}
