package lead.exchange.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import lead.exchange.model.EstateAttributes;
import lead.exchange.samolet.RealtyEstateApiModel;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(
    componentModel = SPRING
)
public interface EstateMapper {


    EstateAttributes toEntity(RealtyEstateApiModel model);
}
