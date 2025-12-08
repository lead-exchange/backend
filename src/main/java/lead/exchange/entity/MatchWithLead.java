package lead.exchange.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lead.exchange.model.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchWithLead {

    private UUID id;
    private UUID leadId;
    private UUID estateId;
    private Double leadCommission;
    private UUID updatedBy;
    private String comment;
    private MatchStatus leadStatus;
    private MatchStatus estateStatus;
    private MatchStatus commonStatus;
    private LocalDateTime matchedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String leadName;

}
