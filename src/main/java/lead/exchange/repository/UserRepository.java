package lead.exchange.repository;

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

    @Query("UPDATE users SET chat_id = :chatId, phone_number = :phoneNumber, updated_at = now() WHERE telegram_id = :telegramId")
    void updateChatAndPhoneByTelegramId(String telegramId, Long chatId, String phoneNumber);
}