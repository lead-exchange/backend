package lead.exchange.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lead.exchange.model.EstateAttributes;
import lead.exchange.samolet.RealtyEstateApiModel;
import lead.exchange.samolet.RealtyEstateApiModel.Address;
import lead.exchange.samolet.RealtyEstateApiModel.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(
    componentModel = SPRING
)
public interface EstateMapper {


    @Mapping(target = "photos", expression = "java(getPhotos(model.getPhotos()))")
    @Mapping(target = "title", expression = "java(getTitle(model))")
    EstateAttributes toEntity(RealtyEstateApiModel model);

    EstateAttributes.Address toEntity(Address model);

    default List<String> getPhotos(List<Photo> photos) {
        return photos.stream()
            .filter(photo -> photo.getBig() != null || photo.getSmall() != null)
            .map(p -> {
                if (p.getBig() != null) {
                    return p.getBig();
                } else {
                    return p.getSmall();
                }
            }).toList();
    }

    default String getTitle(RealtyEstateApiModel model) {
        if (model.getTitle() != null) {
            return model.getTitle();
        }
        Address address = model.getAddress();

        if (address == null) {
            return "";
        }

        return Stream.of(
                address.getRegionName(),
                address.getRegionType(),
                address.getCityType(),
                address.getCityName(),
                address.getPlaceType(),
                address.getPlaceName(),
                address.getStreetType(),
                address.getStreetName(),
                address.getHouse(),
                address.getCorpus(),
                address.getLitera(),
                address.getBuilding()
            )
            .filter(value -> value != null && !value.trim().isEmpty())
            .map(String::trim)
            .collect(Collectors.joining(" "))
            .trim();
    }

}
