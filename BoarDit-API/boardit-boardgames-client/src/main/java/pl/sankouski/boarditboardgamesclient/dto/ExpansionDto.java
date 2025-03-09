package pl.sankouski.boarditboardgamesclient.dto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ExpansionDto {

    private Long bggId;
    @XmlElement(name = "name")
    private List<Name> names;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "yearpublished")
    private YearPublished yearPublished;

    @XmlElement(name = "minplayers")
    private MinPlayers minPlayers;

    @XmlElement(name = "maxplayers")
    private MaxPlayers maxPlayers;

    @XmlElement(name = "playingtime")
    private PlayingTime playingTime;

    @XmlElement(name = "image")
    private String imageLink;

    public ExpansionDto() {
    }

    public ExpansionDto(Long bggId, List<Name> names, String description, YearPublished yearPublished,
                        MinPlayers minPlayers, MaxPlayers maxPlayers, PlayingTime playingTime, String imageLink) {
        this.bggId = bggId;
        this.names = names;
        this.description = description;
        this.yearPublished = yearPublished;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playingTime = playingTime;
        this.imageLink = imageLink;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class YearPublished{

        @XmlAttribute(name = "value")
        private int value;

        public YearPublished setValue(int value) {
            this.value = value;
            return this;
        }

        public int getValue() {
            return value;
        }
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MinPlayers{

        @XmlAttribute(name = "value")
        private int value;

        public MinPlayers setValue(int value) {
            this.value = value;
            return this;
        }

        public int getValue() {
            return value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MaxPlayers{

        @XmlAttribute(name = "value")
        private int value;

        public MaxPlayers setValue(int value) {
            this.value = value;
            return this;
        }
        public int getValue() {
            return value;
        }
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PlayingTime{

        @XmlAttribute(name = "value")
        private int value;

        public PlayingTime setValue(int value) {
            this.value = value;
            return this;
        }

        public int getValue() {
            return value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Name {
        @XmlAttribute(name = "type")
        private String type;

        @XmlAttribute(name = "value")
        private String value;

        public Name() {
        }

        public Name(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    public String getPrimaryName() {
        return names.stream()
                .filter(name -> "primary".equals(name.getType()))
                .map(Name::getValue)
                .findFirst()
                .orElse(null);
    }

    public Long getBggId() {
        return bggId;
    }

    public ExpansionDto setBggId(Long bggId) {
        this.bggId = bggId;
        return this;
    }

    public List<Name> getNames() {
        return names;
    }

    public ExpansionDto setNames(List<Name> names) {
        this.names = names;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpansionDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public YearPublished getYearPublished() {
        return yearPublished;
    }

    public ExpansionDto setYearPublished(YearPublished yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }

    public MinPlayers getMinPlayers() {
        return minPlayers;
    }

    public ExpansionDto setMinPlayers(MinPlayers minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public MaxPlayers getMaxPlayers() {
        return maxPlayers;
    }

    public ExpansionDto setMaxPlayers(MaxPlayers maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

    public PlayingTime getPlayingTime() {
        return playingTime;
    }

    public ExpansionDto setPlayingTime(PlayingTime playingTime) {
        this.playingTime = playingTime;
        return this;
    }

    public String getImageLink() {
        return imageLink;
    }

    public ExpansionDto setImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }
}
