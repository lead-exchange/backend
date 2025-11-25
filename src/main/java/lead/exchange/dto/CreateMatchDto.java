package lead.exchange.dto;

import java.util.UUID;
import lead.exchange.model.MatchStatus;

public record CreateMatchDto(
    UUID leadId,
    UUID estateId,
    Double leadCommission,
    UUID updatedBy,
    String comment,
    MatchStatus status
) {

}
