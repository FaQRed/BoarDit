package pl.sankouski.boarditboardgamesclient.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemsBoardGame {
    public ItemsBoardGame setItems(List<BoardGameDto> items) {
        this.items = items;
        return this;
    }

    @XmlElement(name = "item")
    private List<BoardGameDto> items;

    public List<BoardGameDto> getItems() {
        return items;
    }

}
