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
public class EstateAttributes {
    // TODO: это примеры полей, конкретные поля нужно указать при реализации логики /create для объекта
    private String title;
    private String description;
    private String address;
    private Double price;
    private Integer area;
    private Integer bedrooms;
    private List<String> photos;
}
