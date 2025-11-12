package lead.exchange.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("recommendations")
public class Recommendation {
    @Id
    @Column("id")
    private UUID id;
    @Column("source_id")
    private UUID sourceId;
    @Column("source_type")
    private String sourceType;
    @Column("target_id")
    private UUID targetId;
    @Column("similarity_score")
    private double score;
    @Column("similarity_reason")
    private String reason;
}
