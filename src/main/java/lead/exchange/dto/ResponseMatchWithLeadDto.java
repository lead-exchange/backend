package lead.exchange.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lead.exchange.model.MatchCommonStatus;
import lead.exchange.model.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMatchWithLeadDto {

    private UUID id;
    private UUID leadId;
    private UUID estateId;
    private Double leadCommission;
    private UUID updatedBy;
    private String comment;
    private MatchStatus leadStatus;
    private MatchStatus estateStatus;
    private MatchCommonStatus commonStatus;
    private LocalDateTime matchedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String leadName;

}
