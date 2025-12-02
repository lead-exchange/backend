package lead.exchange.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lead.exchange.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, UUID> {

    @Query("SELECT * FROM users WHERE telegram_id = :telegramId")
    Optional<User> findByTelegramId(String telegramId);

    @Query("SELECT * FROM users WHERE id = :userId")
    Optional<User> findByUserId(UUID userId);

    @Query("SELECT phone FROM users WHERE id = :userId")
    Optional<String> getUserPhone(UUID userId);

    @Query("SELECT * FROM users ORDER BY id LIMIT :limit OFFSET :offset")
    List<User> findBatch(long limit, long offset);
}
