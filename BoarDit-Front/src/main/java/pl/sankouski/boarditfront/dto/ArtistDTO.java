package pl.sankouski.boarditfront.dto;

public class ArtistDTO {
    private Long id;
    private String name;

    public ArtistDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ArtistDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}