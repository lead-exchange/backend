package lead.exchange.repository;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.Estate;
import lead.exchange.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EstateRepositoryTest extends IntegrationTest {

    @Autowired
    private EstateRepository estateRepository;

    @Test
    @Rollback
    @Transactional
    void save_shouldSaveEstateWithAttributes() {
        User user = testData.createTestUser();
        Estate estate = testData.createTestEstate(user.getId());

        assertNotNull(estate.getId());
        assertEquals(user.getId(), estate.getUserId());
        assertEquals("AVAILABLE", estate.getStatus());
        assertNotNull(estate.getAttributes());
        assertEquals("Beautiful Apartment", estate.getAttributes().getTitle());
        assertEquals(150000.0, estate.getAttributes().getPrice());
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnEstate() {
        User user = testData.createTestUser();
        Estate estate = testData.createTestEstate(user.getId());

        Optional<Estate> found = estateRepository.findById(estate.getId());

        assertTrue(found.isPresent());
        assertEquals(estate, found.get());
    }

    @Test
    @Rollback
    @Transactional
    void findByUserId_shouldReturnUserEstates() {
        User user = testData.createTestUser();
        testData.createTestEstate(user.getId());
        testData.createTestEstate(user.getId());

        List<Estate> estates = estateRepository.findByUserId(user.getId());

        assertEquals(2, estates.size());
        estates.forEach(estate -> assertEquals(user.getId(), estate.getUserId()));
    }
}