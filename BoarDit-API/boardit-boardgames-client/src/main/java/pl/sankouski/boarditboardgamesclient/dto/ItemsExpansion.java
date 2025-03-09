package pl.sankouski.boarditboardgamesclient.dto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemsExpansion {
    @XmlElement(name = "item")
    private List<ExpansionDto> items;

    public List<ExpansionDto> getItems() {
        return items;
    }
}
