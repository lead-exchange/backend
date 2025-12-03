package lead.exchange.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
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

@Component
@RequiredArgsConstructor
@Mapper(componentModel = SPRING)
public abstract class MatchMapper {

    @Autowired
    private Clock clock;

    public MatchUpdateEntity toEntity(UpdateMatchDto matchDto, UUID userId) {
        LocalDateTime now = LocalDateTime.now(clock);
        return new MatchUpdateEntity(
            matchDto.id(),
            matchDto.leadCommission(),
            userId,
            matchDto.comment(),
            matchDto.status(),
            now
        );
    }

    public Match toEntity(CreateMatchDto matchDto, UUID userId) {
        LocalDateTime now = LocalDateTime.now(clock);

        return Match.builder()
            .leadId(matchDto.leadId())
            .estateId(matchDto.estateId())
            .leadCommission(matchDto.leadCommission())
            .comment(matchDto.comment())
            .updatedBy(userId)
            .leadStatus(MatchStatus.UNDEFINED)
            .estateStatus(MatchStatus.UNDEFINED)
            .updatedAt(now)
            .matchedAt(now)
            .createdAt(now)
            .build();
    }


    public abstract ResponseMatchWithLeadDto toDto(MatchWithLead match);

    @Mapping(target = "estatePhoto", expression = "java(getPhotos(match.getEstatePhotos()))")
    public abstract ResponseMatchWithEstateDto toDto(MatchWithEstate match);

    protected String getPhotos(List<String> photos) {
        if (photos == null || photos.isEmpty()) {
            return null;
        }
        return photos.getFirst();
    }
}
