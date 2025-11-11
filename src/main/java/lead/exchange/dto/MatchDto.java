package lead.exchange.dto;

import lead.exchange.model.MatchStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record MatchDto(
        UUID id,
        UUID leadId,
        UUID estateId,
        Double leadCommission,
        UUID updatedBy,
        String comment,
        MatchStatus leadStatus,
        MatchStatus estateStatus,
        LocalDateTime matchedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UserType userType
) {
}
