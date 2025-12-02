package lead.exchange.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lead.exchange.model.EstateAttributes;
import lead.exchange.model.EstateStatus;
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
@Table("estates")
public class Estate {
    @Id
    @Column("id")
    private UUID id;
    @Column("external_id")
    private Long externalId;
    @Column("user_id")
    private UUID userId;
    private EstateAttributes attributes;
    @Column("total_commission_rate")
    private Double totalCommissionRate;
    @Column("commission_share")
    private Double commissionShare;
    private EstateStatus status;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.truncatedTo(ChronoUnit.MICROS);
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt.truncatedTo(ChronoUnit.MICROS);
    }

    public static class EstateBuilder {

        public EstateBuilder createdAt(LocalDateTime timestamp) {
            this.createdAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public EstateBuilder updatedAt(LocalDateTime timestamp) {
            this.updatedAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }
    }
}
