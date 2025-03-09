package pl.sankouski.boarditdata.dto;

import java.util.List;

public class BoardGameDTO {
    private Long id;
    private String name;
    private String description;
    private int yearPublished;
    private int minPlayers;
    private int maxPlayers;
    private int playingTime;
    private String imageLink;
    private List<Long> categories;
    private List<Long> mechanics;
    private List<String> artists;
    private List<String> alternateNames;

    public BoardGameDTO(Long id, String name, String description, int yearPublished, int minPlayers, int maxPlayers,
                        int playingTime, String imageLink, List<Long> categories, List<Long> mechanics,
                        List<String> artists, List<String> alternateNames) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.yearPublished = yearPublished;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playingTime = playingTime;
        this.imageLink = imageLink;
        this.categories = categories;
        this.mechanics = mechanics;
        this.artists = artists;
        this.alternateNames = alternateNames;
    }

    public BoardGameDTO() {
    }

    public String getName() {
        return name;
    }

    public BoardGameDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BoardGameDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public BoardGameDTO setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public BoardGameDTO setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public BoardGameDTO setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public int getPlayingTime() {
        return playingTime;
    }

    public BoardGameDTO setPlayingTime(int playingTime) {
        this.playingTime = playingTime;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public BoardGameDTO setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public BoardGameDTO setCategories(List<Long> categories) {
        this.categories = categories;
        return this;
    }

    public List<Long> getMechanics() {
        return mechanics;
    }

    public BoardGameDTO setMechanics(List<Long> mechanics) {
        this.mechanics = mechanics;
        return this;
    }

    public List<String> getArtists() {
        return artists;
    }

    public BoardGameDTO setArtists(List<String> artists) {
        this.artists = artists;
        return this;
    }

    public List<String> getAlternateNames() {
        return alternateNames;
    }

    public BoardGameDTO setAlternateNames(List<String> alternateNames) {
        this.alternateNames = alternateNames;
        return this;
    }

    public Long getId() {
        return id;
    }

    public BoardGameDTO setId(Long id) {
        this.id = id;
        return this;
    }
}