package lead.exchange.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lead.exchange.model.MatchStatus;

public record ResponseMatchWithEstateDto(
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
        String estateTitle,
        String estatePhoto
) {
}
