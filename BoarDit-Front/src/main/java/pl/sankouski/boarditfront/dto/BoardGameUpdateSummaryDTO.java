package pl.sankouski.boarditfront.dto;

public class BoardGameUpdateSummaryDTO {
    private Long id;
    private String name;
    private String description;
    private int yearPublished;
    private int minPlayers;
    private int maxPlayers;
    private int playingTime;
    private String imageLink;

    public BoardGameUpdateSummaryDTO() {
    }

    public BoardGameUpdateSummaryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public BoardGameUpdateSummaryDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BoardGameUpdateSummaryDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BoardGameUpdateSummaryDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public BoardGameUpdateSummaryDTO setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public BoardGameUpdateSummaryDTO setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public BoardGameUpdateSummaryDTO setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public int getPlayingTime() {
        return playingTime;
    }

    public BoardGameUpdateSummaryDTO setPlayingTime(int playingTime) {
        this.playingTime = playingTime;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public BoardGameUpdateSummaryDTO setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }
}