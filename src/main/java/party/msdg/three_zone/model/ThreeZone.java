package party.msdg.three_zone.model;

import lombok.Data;

/**
 * Wow! Sweet moon.
 */
@Data
public class ThreeZone {
    
    private String zone;
    
    private String town;
    
    private String street;
    
    private String area;
    
    private String type;
    
    public ThreeZone(String zone, String town, String street, String area, String type) {
        this.zone = zone;
        this.town = town;
        this.street = street;
        this.area = area;
        this.type = type;
    }
}
