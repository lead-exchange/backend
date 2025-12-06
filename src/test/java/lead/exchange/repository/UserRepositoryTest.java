package lead.exchange.repository;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    @Transactional
    void saveAndFindUser() {
        User user = testData.createTestUserWithTelegramId(123l);

        Optional<User> foundUser = userRepository.findByTelegramId(123l);

        assertTrue(foundUser.isPresent());

        User actual = foundUser.get();

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getTelegramId(), actual.getTelegramId());
    }
}