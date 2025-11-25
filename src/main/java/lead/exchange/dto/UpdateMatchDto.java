package lead.exchange.dto;

import java.util.UUID;
import lead.exchange.model.MatchStatus;

public record UpdateMatchDto(
        UUID id,
        Double leadCommission,
        UUID updatedBy,
        String comment,
        MatchStatus status
) {
}
