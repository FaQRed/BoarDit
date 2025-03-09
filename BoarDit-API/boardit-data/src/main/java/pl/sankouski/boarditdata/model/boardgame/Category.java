package pl.sankouski.boarditdata.model.boardgame;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bggId;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private List<BoardGame> boardGames;

    public Long getId() {
        return id;
    }

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getBggId() {
        return bggId;
    }

    public Category setBggId(Long bggId) {
        this.bggId = bggId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public List<BoardGame> getBoardGames() {
        return boardGames;
    }

    public Category setBoardGames(List<BoardGame> boardGames) {
        this.boardGames = boardGames;
        return this;
    }


}
