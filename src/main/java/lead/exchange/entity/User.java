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
@Table("users")
public class User {
    @Id
    @Column("id")
    private UUID id;
    @Column("telegram_id")
    private Long telegramId;
    @Column("chat_id")
    private Long chatId;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("phone")
    private String phone;
    @Column("offer1_signed")
    private Boolean offer1Signed;
    @Column("offer2_signed")
    private Boolean offer2Signed;
    @Column("telegram_username")
    private String telegramUsername;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;

    public static class UserBuilder {

        public UserBuilder createdAt(LocalDateTime timestamp) {
            this.createdAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }

        public UserBuilder updatedAt(LocalDateTime timestamp) {
            this.updatedAt = timestamp.truncatedTo(ChronoUnit.MICROS);
            return this;
        }
    }
}
