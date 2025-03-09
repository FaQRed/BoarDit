package pl.sankouski.boarditdata.dto;

public class MechanicDTO {
    private Long id;
    private String name;

    public MechanicDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MechanicDTO() {
    }

    public Long getId() {
        return id;
    }

    public MechanicDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MechanicDTO setName(String name) {
        this.name = name;
        return this;
    }
}