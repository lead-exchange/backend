package lead.exchange.service;

import java.util.UUID;
import lead.exchange.entity.User;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void checkUserExistByUserid(UUID userId) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with userId: " + userId
                ));
    }
}
