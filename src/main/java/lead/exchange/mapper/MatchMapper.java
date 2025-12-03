package lead.exchange.mapper;

import java.time.Clock;
import java.time.LocalDateTime;
import lead.exchange.dto.CreateMatchDto;
import lead.exchange.dto.ResponseMatchWithEstateDto;
import lead.exchange.dto.ResponseMatchWithLeadDto;
import lead.exchange.dto.UpdateMatchDto;
import lead.exchange.entity.Match;
import lead.exchange.entity.MatchUpdateEntity;
import lead.exchange.entity.MatchWithEstate;
import lead.exchange.entity.MatchWithLead;
import lead.exchange.model.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Component
@RequiredArgsConstructor
@Mapper(componentModel = SPRING)
public abstract class MatchMapper {

    @Autowired
    private Clock clock;

    public MatchUpdateEntity toEntity(UpdateMatchDto matchDto) {
        LocalDateTime now = LocalDateTime.now(clock);
        return new MatchUpdateEntity(
            matchDto.id(),
            matchDto.leadCommission(),
            matchDto.updatedBy(),
            matchDto.comment(),
            matchDto.status(),
            now
        );
    }

    public Match toEntity(CreateMatchDto matchDto) {
        LocalDateTime now = LocalDateTime.now(clock);

        return Match.builder()
            .leadId(matchDto.leadId())
            .estateId(matchDto.estateId())
            .leadCommission(matchDto.leadCommission())
            .updatedBy(matchDto.updatedBy())
            .comment(matchDto.comment())
            .leadStatus(MatchStatus.UNDEFINED)
            .estateStatus(MatchStatus.UNDEFINED)
            .updatedAt(now)
            .matchedAt(now)
            .createdAt(now)
            .build();
    }


    public abstract ResponseMatchWithLeadDto toDto(MatchWithLead match);

    @Mapping(target = "estatePhoto", expression = "java(match.getEstatePhotos().getFirst())")
    public abstract ResponseMatchWithEstateDto toDto(MatchWithEstate match);
}
