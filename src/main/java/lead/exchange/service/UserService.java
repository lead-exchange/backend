package lead.exchange.service;

import java.util.List;
import java.util.Optional;
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

    public User getUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with telegramId: " + telegramId
                ));
    }

    public Optional<User> getOptionalUserByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
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

    public User updatePhone(UUID userId, String newPhone) {
        User user = getUserById(userId);

        if (newPhone.equals(user.getPhone())) {
            return user;
        }

        user.setPhone(newPhone);

        topnlabApiService.updateEstatesConcurently(userId, newPhone);

        return saveUser(user);
    }

    public User signOffer1(UUID userId) {
        User user = getUserById(userId);

        if (Boolean.TRUE.equals(user.getOffer1Signed())) {
            return user;
        }

        user.setOffer1Signed(true);
        return saveUser(user);
    }

    public User signOffer2(UUID userId) {
        User user = getUserById(userId);

        if (Boolean.TRUE.equals(user.getOffer2Signed())) {
            return user;
        }

        user.setOffer2Signed(true);
        return saveUser(user);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User resetUserData(UUID userId) {
        User user = getUserById(userId);

        user.setPhone(null);
        user.setOffer1Signed(false);
        user.setOffer2Signed(false);

        return saveUser(user);
    }

}
