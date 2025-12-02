package lead.exchange.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstateAttributes {
    private String title;
    private String description;
    private Address address;
    private String realtyType;

    private Double areaCommon;
    private Double areaKitchen;
    private Double areaLiving;
    private String areaRoom;
    private Double areaLand;
    private Integer areaLandType;

    private String builtYear;
    private Integer floor;
    private Integer floors;
    private Integer rooms;
    private Integer roofHeight;

    private Long price;
    private Long pricePerMeter;

    private List<String> photos;

    @Data
    @NoArgsConstructor
    public static class Address {

        private String coordinates;
        private String regionName;
        private String regionType;
        private Object countyName;
        private Object cityName;
        private Object placeName;
        private Object placeType;
        private Object streetName;
        private Object streetType;
        private String house;
        private Object corpus;
        private Object litera;
        private Object building;
        private Object metro;
        private String flat;
    }
}
