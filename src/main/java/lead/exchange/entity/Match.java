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

    @Column("lead_commission")
    private Double leadCommission;

    @Column("updated_by")
    private UUID updatedBy;

    @Column("comment")
    private String comment;

    @Column("lead_status")
    private MatchStatus leadStatus;

    @Column("estate_status")
    private MatchStatus estateStatus;

    @Column("matched_at")
    private LocalDateTime matchedAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public static class MatchBuilder {

        public Match.MatchBuilder createdAt(LocalDateTime timestamp) {
            this.createdAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public Match.MatchBuilder updatedAt(LocalDateTime timestamp) {
            this.updatedAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public Match.MatchBuilder matchedAt(LocalDateTime timestamp) {
            this.matchedAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }
    }
}
