package lead.exchange.external;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import lead.exchange.model.EstateAttributes;
import org.springframework.stereotype.Component;


@Component
public class ExternalEstateMapper {

    public EstateAttributes toAttributes(JsonNode node) {

        EstateAttributes a = new EstateAttributes();

        a.setAddress(node.path("address").asText(null));
        a.setRealtyType(node.path("realty_type").asText(null));

        a.setAreaCommon(getDouble(node, "area_common"));
        a.setAreaKitchen(getDouble(node, "area_kitchen"));
        a.setAreaLiving(getDouble(node, "area_living"));
        a.setAreaRoom(node.path("area_room").asText(null));
        a.setAreaLand(getDouble(node, "area_land"));

        a.setBuiltYear(node.path("built_year").asText(null));
        a.setFloor(getInt(node, "floor"));
        a.setFloors(getInt(node, "floors"));
        a.setRooms(getInt(node, "rooms"));
        a.setRoofHeight(getInt(node, "roof_height"));

        a.setPrice(getLong(node, "price"));
        a.setPricePerMeter(getLong(node, "price_per_meter"));

        String photosField = "photos";
        String bigField = "big";
        String smallField = "small";

        List<String> photos = new ArrayList<>();
        if (node.has(photosField) && node.get(photosField).isArray()) {
            node.get(photosField).forEach(p -> {
                if (p.has(bigField) && p.path(bigField).asText(null) != null) {
                    String url = p.path(bigField).asText(null);
                    photos.add(url);
                } else if (p.has(smallField) && p.path(smallField).asText(null) != null) {
                    String url = p.path(smallField).asText(null);
                    photos.add(url);
                }
            });
        }
        a.setPhotos(photos);

        return a;
    }

    private Double getDouble(JsonNode node, String field) {
        return node.path(field).isNumber() ? node.path(field).asDouble() : null;
    }

    private Integer getInt(JsonNode node, String field) {
        return node.path(field).isNumber() ? node.path(field).asInt() : null;
    }

    private Long getLong(JsonNode node, String field) {
        return node.path(field).isNumber() ? node.path(field).asLong() : null;
    }
}
