package lead.exchange.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lead.exchange.model.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("matches")
public class Match {
    @Id
    @Column("id")
    private UUID id;
    @Column("lead_id")
    private UUID leadId;
    @Column("estate_id")
    private UUID estateId;
    private MatchStatus status;
    @Column("matched_at")
    private LocalDateTime matchedAt;

    public static class MatchBuilder {
        public MatchBuilder matchedAt(LocalDateTime timestamp) {
            this.matchedAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }
    }
}
