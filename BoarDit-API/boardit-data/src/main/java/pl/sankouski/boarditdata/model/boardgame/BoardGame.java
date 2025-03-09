package pl.sankouski.boarditdata.model.boardgame;
import jakarta.persistence.*;
import java.util.List;


@Entity
@Table(name = "board_game")
public class BoardGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

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
    private Long bggId;



    @OneToMany(mappedBy = "boardGame", cascade = CascadeType.ALL)
    private List<AlternateName> alternateNames;


    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "board_game_category")
    private List<Category> categories;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "board_game_mechanic")
    private List<Mechanic> mechanics;

    @OneToMany(mappedBy = "boardGame", cascade = CascadeType.ALL)
    private List<Expansion> expansions;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "board_game_artist")
    private List<Artist> artists;


    public Long getId() {
        return id;
    }

    public BoardGame setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BoardGame setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BoardGame setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public BoardGame setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }


    public Long getBggId() {
        return bggId;
    }

    public BoardGame setBggId(Long bggId) {
        this.bggId = bggId;
        return this;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public BoardGame setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public BoardGame setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public int getPlayingTime() {
        return playingTime;
    }

    public BoardGame setPlayingTime(int playingTime) {
        this.playingTime = playingTime;
        return this;
    }

    public List<AlternateName> getAlternateNames() {
        return alternateNames;
    }

    public BoardGame setAlternateNames(List<AlternateName> alternateNames) {
        this.alternateNames = alternateNames;
        return this;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public BoardGame setCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public List<Mechanic> getMechanics() {
        return mechanics;
    }

    public BoardGame setMechanics(List<Mechanic> mechanics) {
        this.mechanics = mechanics;
        return this;
    }

    public List<Expansion> getExpansions() {
        return expansions;
    }

    public BoardGame setExpansions(List<Expansion> expansions) {
        this.expansions = expansions;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public BoardGame setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public BoardGame setArtists(List<Artist> artists) {
        this.artists = artists;
        return this;
    }
}