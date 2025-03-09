package pl.sankouski.boarditdata.model.boardgame;


import jakarta.persistence.*;

@Entity
public class Expansion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long bggId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "board_game_id")
    private BoardGame boardGame;

    @Column(length = 10000)
    private String description;
    @Column(nullable = false)
    private int yearPublished;
    @Column(nullable = false)
    private int minPlayers;
    @Column(nullable = false)
    private int maxPlayers;
    private int playingTime;

    private String imageLink;

    public String getDescription() {
        return description;
    }

    public Expansion setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public Expansion setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public Expansion setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Expansion setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public int getPlayingTime() {
        return playingTime;
    }

    public Expansion setPlayingTime(int playingTime) {
        this.playingTime = playingTime;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public Expansion setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public Long getBggId() {
        return bggId;
    }

    public Expansion setBggId(Long bggId) {
        this.bggId = bggId;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Expansion setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Expansion setName(String name) {
        this.name = name;
        return this;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public Expansion setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
        return this;
    }
}
