package pl.sankouski.boarditboardgamesclient.dto;


import javax.xml.bind.annotation.*;
import java.util.List;

import java.util.stream.Collectors;




@XmlAccessorType(XmlAccessType.FIELD)

public class BoardGameDto {


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

        @XmlElement(name = "link")
        private List<Link> links;



        public BoardGameDto() {
        }


        public BoardGameDto(Long bggId, List<Name> names, String description, YearPublished yearPublished,
                            MinPlayers minPlayers, MaxPlayers maxPlayers, PlayingTime playingTime,
                            String imageLink, List<Link> links) {
                this.bggId = bggId;
                this.names = names;
                this.description = description;
                this.yearPublished = yearPublished;
                this.minPlayers = minPlayers;
                this.maxPlayers = maxPlayers;
                this.playingTime = playingTime;
                this.imageLink = imageLink;
                this.links = links;
        }

        public String getPrimaryName() {
                return names.stream()
                        .filter(name -> "primary".equals(name.getType()))
                        .map(Name::getValue)
                        .findFirst()
                        .orElse(null);
        }

        public List<String> getAlternateNames() {
                return names.stream()
                        .filter(name -> "alternate".equals(name.getType()))
                        .map(Name::getValue)
                        .collect(Collectors.toList());
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        public static class YearPublished{

                @XmlAttribute(name = "value")
                private int value;

                public int getValue() {
                        return value;
                }

                public YearPublished setValue(int value) {
                        this.value = value;
                        return this;
                }
        }
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class MinPlayers{

                @XmlAttribute(name = "value")
                private int value;

                public int getValue() {
                        return value;
                }
                public MinPlayers setValue(int value) {
                        this.value = value;
                        return this;
                }
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class MaxPlayers{

                @XmlAttribute(name = "value")
                private int value;

                public int getValue() {
                        return value;
                }
                public MaxPlayers setValue(int value) {
                        this.value = value;
                        return this;
                }
        }
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class PlayingTime{

                @XmlAttribute(name = "value")
                private int value;

                public int getValue() {
                        return value;
                }
                public PlayingTime setValue(int value) {
                        this.value = value;
                        return this;
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

        @XmlAccessorType(XmlAccessType.FIELD)
        public static class Link {
                @XmlAttribute(name = "type")
                private String type;

                @XmlAttribute(name = "value")
                private String value;

                @XmlAttribute(name = "id")
                private String id;


                public Link() {
                }


                public Link(String type, String value, String id) {
                        this.type = type;
                        this.value = value;
                        this.id = id;
                }

                public String getType() {
                        return type;
                }

                public String getValue() {
                        return value;
                }

                public String getId() {
                        return id;
                }
        }

        public Long getBggId() {
                return bggId;
        }

        public BoardGameDto setBggId(Long bggId) {
                this.bggId = bggId;
                return this;
        }

        public List<Name> getNames() {
                return names;
        }

        public void setNames(List<Name> names) {
                this.names = names;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public YearPublished getYearPublished() {
                return yearPublished;
        }

        public BoardGameDto setYearPublished(YearPublished yearPublished) {
                this.yearPublished = yearPublished;
                return this;
        }

        public MinPlayers getMinPlayers() {
                return minPlayers;
        }

        public BoardGameDto setMinPlayers(MinPlayers minPlayers) {
                this.minPlayers = minPlayers;
                return this;
        }

        public MaxPlayers getMaxPlayers() {
                return maxPlayers;
        }

        public BoardGameDto setMaxPlayers(MaxPlayers maxPlayers) {
                this.maxPlayers = maxPlayers;
                return this;
        }

        public PlayingTime getPlayingTime() {
                return playingTime;
        }

        public BoardGameDto setPlayingTime(PlayingTime playingTime) {
                this.playingTime = playingTime;
                return this;
        }

        public String getImageLink() {
                return imageLink;
        }

        public void setImageLink(String imageLink) {
                this.imageLink = imageLink;
        }

        public List<Link> getLinks() {
                return links;
        }

        public void setLinks(List<Link> links) {
                this.links = links;
        }


}