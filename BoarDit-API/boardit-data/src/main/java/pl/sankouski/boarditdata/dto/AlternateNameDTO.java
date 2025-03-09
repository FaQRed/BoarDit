package pl.sankouski.boarditdata.dto;


public class AlternateNameDTO {
    private String name;
    private long id;

    public AlternateNameDTO(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public AlternateNameDTO setId(long id) {
        this.id = id;
        return this;
    }

    public AlternateNameDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}