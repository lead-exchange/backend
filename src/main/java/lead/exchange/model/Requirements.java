package lead.exchange.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Requirements {

    private String propertyType;
    private String description;

    private Double minPrice;
    private Double maxPrice;

    private Integer minArea;
    private Integer maxArea;

    private Integer minKitchenArea;
    private Integer maxKitchenArea;

    private RenovationType renovation = RenovationType.ANY;

    private List<String> locations;
    private Integer bedrooms;
}
