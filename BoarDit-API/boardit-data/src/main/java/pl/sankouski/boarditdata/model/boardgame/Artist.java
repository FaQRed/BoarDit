package pl.sankouski.boarditdata.model.boardgame;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bggId;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "artists")
    private List<BoardGame> boardGames = new ArrayList<>();

    public Artist(String name, List<BoardGame> boardGames) {
        this.name = name;
        this.boardGames = boardGames;
    }

    public Artist() {
    }

    public Long getId() {
        return id;
    }

    public Long getBggId() {
        return bggId;
    }

    public Artist setBggId(Long bggId) {
        this.bggId = bggId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Artist setId(Long id) {
        this.id = id;
        return this;
    }

    public Artist setName(String name) {
        this.name = name;
        return this;
    }

    public List<BoardGame> getBoardGames() {
        return boardGames;
    }

    public Artist setBoardGames(List<BoardGame> boardGames) {
        this.boardGames = boardGames;
        return this;
    }
}
