package lead.exchange.mapper;

import java.util.List;
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

}
