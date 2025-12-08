package lead.exchange.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lead.exchange.model.MatchCommonStatus;
import lead.exchange.model.MatchStatus;

public record MatchUpdateEntity(
    UUID id,
    Double leadCommission,
    UUID updatedBy,
    String comment,
    MatchCommonStatus commonStatus,
    MatchStatus status,
    LocalDateTime updatedAt
) {

}
