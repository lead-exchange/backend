package lead.exchange.service;

import java.util.Optional;
import java.util.UUID;
import lead.exchange.entity.User;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with telegramId: " + telegramId
                ));
    }

    public void checkUserExistByUserId(UUID userId) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with userId: " + userId
                ));
    }

    public void savePhoneAndChat(String telegramId, Long chatId, String phoneNumber) {
        Optional<User> userOpt = userRepository.findByTelegramId(telegramId);

        if (userOpt.isPresent()) {
            userRepository.updateChatAndPhoneByTelegramId(telegramId, chatId, phoneNumber);
        } else {
            User user = new User(
                    java.util.UUID.randomUUID(),
                    telegramId,
                    phoneNumber,
                    chatId,
                    java.time.LocalDateTime.now(),
                    java.time.LocalDateTime.now()
            );
            userRepository.save(user);
        }
    }
}
