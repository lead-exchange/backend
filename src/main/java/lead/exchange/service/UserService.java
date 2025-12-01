package lead.exchange.service;

import java.util.List;
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

    public String getUserPhone(UUID userId) {
        return userRepository.getUserPhone(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Can't get phone: user not found with userId = " + userId
                ));
    }

    public List<User> getAll(long offset, long batchSize) {
        return userRepository.findBatch(batchSize, offset);
    }

    public User updatePhone(UUID userId, String newPhone) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException(
                "User not found with userId: " + userId
        ));

        if (newPhone.equals(user.getPhone())) {
            return user;
        }

        user.setPhone(newPhone);
        userRepository.save(user);

        return user;
    }
}
