package lead.exchange.repository;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.Estate;
import lead.exchange.entity.User;
import lead.exchange.model.EstateStatus;
import org.junit.jupiter.api.BeforeEach;
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

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = testData.createTestUser();
    }

    @Test
    @Rollback
    @Transactional
    void save_shouldSaveEstateWithAttributes() {
        Estate estate = testData.createTestEstate(testUser.getId());

        assertNotNull(estate.getId());
        assertEquals(testUser.getId(), estate.getUserId());
        assertEquals(EstateStatus.ACTIVE, estate.getStatus());
        assertNotNull(estate.getAttributes());
        assertEquals("Beautiful Apartment", estate.getAttributes().getTitle());
        assertEquals(150000.0, estate.getAttributes().getPrice());
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnEstate() {
        Estate estate = testData.createTestEstate(testUser.getId());

        Optional<Estate> found = estateRepository.findById(estate.getId());

        assertTrue(found.isPresent());

        Estate actual = found.get();

        assertEquals(estate.getId(), actual.getId());
        assertEquals(estate.getUserId(), actual.getUserId());
        assertEquals(estate.getAttributes(), actual.getAttributes());
        assertEquals(estate.getTotalCommissionRate(), actual.getTotalCommissionRate());
        assertEquals(estate.getCommissionShare(), actual.getCommissionShare());
        assertEquals(estate.getStatus(), actual.getStatus());    }

    @Test
    @Rollback
    @Transactional
    void findByUserId_shouldReturnUserEstates() {
        testData.createTestEstate(testUser.getId());
        testData.createTestEstate(testUser.getId());

        List<Estate> estates = estateRepository.findByUserId(testUser.getId());

        assertEquals(2, estates.size());
        estates.forEach(estate -> assertEquals(testUser.getId(), estate.getUserId()));
    }
}