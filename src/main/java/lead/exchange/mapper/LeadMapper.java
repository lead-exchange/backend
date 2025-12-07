package lead.exchange.mapper;

import lead.exchange.dto.LeadCreateDto;
import lead.exchange.entity.Lead;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@Mapper(
    componentModel = SPRING
)
public interface LeadMapper {


    Lead toEntity(LeadCreateDto model);

}
