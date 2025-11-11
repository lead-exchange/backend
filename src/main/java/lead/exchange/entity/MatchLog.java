package lead.exchange.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
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
@Table("matches_log")
public class MatchLog {
    @Id
    @Column("id")
    private UUID id;

    @Column("match_id")
    private UUID matchId;

    private String status;

    @Column("lead_commission")
    private Double leadCommission;

    @Column("updated_by")
    private UUID updatedBy;

    private String comment;

    @Column("created_at")
    private LocalDateTime createdAt;

    public static class MatchLogBuilder {

        public MatchLogBuilder createdAt(LocalDateTime timestamp) {
            this.createdAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }
    }
}