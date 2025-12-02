package lead.exchange.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class EstateAttributes {
    private String title;
    private String description;
    private String address;
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
}
