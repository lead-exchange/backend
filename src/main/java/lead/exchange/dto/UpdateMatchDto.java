package lead.exchange.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lead.exchange.model.MatchStatus;

public record UpdateMatchDto(
        @NotNull
        UUID id,
        @NotNull
        Double leadCommission,
        String comment,
        @NotNull
        MatchStatus status
) {
}
