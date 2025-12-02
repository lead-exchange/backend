package lead.exchange.service;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.User;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.repository.UserRepository;
import lead.exchange.samolet.TopnlabApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TopnlabApiService topnlabApiService;

    public User getUserByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with telegramId: " + telegramId
                ));
    }

    public User getUserById(UUID userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with userId: " + userId
                ));
    }

    public void fillUserEstates(UUID userId) {
        User user = getUserById(userId);
        topnlabApiService.updateEstates(userId, user.getPhone());

    }

    public List<User> getAll(long offset, long batchSize) {
        return userRepository.findBatch(batchSize, offset);
    }
}
