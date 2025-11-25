package lead.exchange.mapper;

import java.time.Clock;
import java.time.LocalDateTime;
import lead.exchange.dto.CreateMatchDto;
import lead.exchange.dto.UpdateMatchDto;
import lead.exchange.entity.Match;
import lead.exchange.entity.MatchUpdateEntity;
import lead.exchange.model.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchMapper {

    private final Clock clock;

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
}
