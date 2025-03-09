package pl.sankouski.boarditfront.dto;

public class ExpansionDTO {
    private Long id;
    private String name;
    private String description;
    private int yearPublished;
    private int minPlayers;
    private int maxPlayers;
    private int playingTime;
    private String imageLink;

    public ExpansionDTO(Long id, String name, String description, int yearPublished,
                        int minPlayers, int maxPlayers, int playingTime, String imageLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.yearPublished = yearPublished;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playingTime = playingTime;
        this.imageLink = imageLink;
    }

    public ExpansionDTO() {
    }

    public ExpansionDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public ExpansionDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExpansionDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpansionDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public ExpansionDTO setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public ExpansionDTO setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public ExpansionDTO setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public int getPlayingTime() {
        return playingTime;
    }

    public ExpansionDTO setPlayingTime(int playingTime) {
        this.playingTime = playingTime;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public ExpansionDTO setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

}