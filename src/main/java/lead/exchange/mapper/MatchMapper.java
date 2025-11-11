package lead.exchange.mapper;

import lead.exchange.dto.MatchDto;
import lead.exchange.entity.Match;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchMapper {
    public static Match toEntity(MatchDto matchDto) {
        return Match.builder()
                .id(matchDto.id())
                .leadId(matchDto.leadId())
                .estateId(matchDto.estateId())
                .leadCommission(matchDto.leadCommission())
                .updatedBy(matchDto.updatedBy())
                .comment(matchDto.comment())
                .leadStatus(matchDto.leadStatus())
                .estateStatus(matchDto.estateStatus())
                .matchedAt(matchDto.matchedAt())
                .createdAt(matchDto.createdAt())
                .updatedAt(matchDto.updatedAt())
                .build();
    }
}
