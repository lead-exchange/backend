package lead.exchange.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lead.exchange.model.MatchStatus;

public record MatchUpdateEntity(
    UUID id,
    Double leadCommission,
    UUID updatedBy,
    String comment,
    MatchStatus status,
    LocalDateTime updatedAt
) {

}
