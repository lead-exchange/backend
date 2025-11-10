package lead.exchange.entity;

import java.sql.Timestamp;
import java.util.UUID;
import lead.exchange.model.LeadStatus;
import lead.exchange.model.Requirements;
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
@Table("leads")
public class Lead {
    @Id
    @Column("id")
    private UUID id;
    @Column("user_id")
    private UUID userId;
    private Requirements requirements;
    private LeadStatus status;
    @Column("commission_share")
    private Double commissionShare;
    @Column("created_at")
    private Timestamp createdAt;
    @Column("updated_at")
    private Timestamp updatedAt;

//    public static class LeadBuilder {
//        public LeadBuilder createdAt(LocalDateTime timestamp) {
//            this.createdAt = timestamp.truncatedTo(ChronoUnit.MICROS);
//            return this;
//        }
//
//        public LeadBuilder updatedAt(LocalDateTime timestamp) {
//            this.updatedAt = timestamp.truncatedTo(ChronoUnit.MICROS);
//            return this;
//        }
//    }
}
