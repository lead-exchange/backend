package lead.exchange.entity;

import java.sql.Timestamp;
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
@Table("users")
public class User {
    @Id
    @Column("id")
    private UUID id;
    @Column("telegram_id")
    private String telegramId;
    @Column("created_at")
    private Timestamp createdAt;
    @Column("updated_at")
    private Timestamp updatedAt;

//    public static class UserBuilder {
//        public UserBuilder createdAt(LocalDateTime timestamp) {
//            this.createdAt = timestamp.truncatedTo(ChronoUnit.MICROS);
//            return this;
//        }
//
//        public UserBuilder updatedAt(LocalDateTime timestamp) {
//            this.updatedAt = timestamp.truncatedTo(ChronoUnit.MICROS);
//            return this;
//        }
//    }
}
