package lead.exchange.repository;

import lead.exchange.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends ListCrudRepository<User, UUID> {

    @Query("SELECT * FROM users WHERE telegram_id = :telegramId")
    Optional<User> findByTelegramId(String telegramId);
}
