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
    // TODO: это примеры полей, конкретные поля нужно указать при реализации логики /create для лида
    private String propertyType;
    private Double minPrice;
    private Double maxPrice;
    private Integer minArea;
    private Integer maxArea;
    private List<String> locations;
    private Integer bedrooms;
}
