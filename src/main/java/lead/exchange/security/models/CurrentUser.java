package lead.exchange.security.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {
    private UUID id;
    private Long telegramId;
    private String username;
}
