package lead.exchange.entity;

import lead.exchange.model.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

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
}
