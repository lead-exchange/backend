package lead.exchange.service;

import lead.exchange.entity.User;
import lead.exchange.exception.ResourceNotFoundException;
import lead.exchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByTelegramId(String telegramId){
        return userRepository.findByTelegramId(telegramId).orElseThrow(()-> new ResourceNotFoundException("User not found with telegramId: " + telegramId));
    }

    public void checkUserExistByUserid(UUID userId){
        userRepository.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with userId: " + userId));
    }
}
