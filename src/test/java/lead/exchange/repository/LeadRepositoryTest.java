package lead.exchange.repository;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.Lead;
import lead.exchange.entity.User;
import lead.exchange.model.LeadStatus;
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
class LeadRepositoryTest extends IntegrationTest {

    @Autowired
    private LeadRepository leadRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = testData.createTestUser();
    }

    @Test
    @Rollback
    @Transactional
    void save_shouldSaveLeadWithRequirements() {
        Lead lead = testData.createTestLead(testUser.getId());

        assertNotNull(lead.getId());
        assertEquals(testUser.getId(), lead.getUserId());
        assertEquals(LeadStatus.ACTIVE, lead.getStatus());
        assertNotNull(lead.getRequirements());
        assertEquals("APARTMENT", lead.getRequirements().getPropertyType());
        assertEquals(100000.0, lead.getRequirements().getMinPrice());
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnLead() {
        Lead lead = testData.createTestLead(testUser.getId());

        Optional<Lead> found = leadRepository.findById(lead.getId());

        assertTrue(found.isPresent());
        assertEquals(lead, found.get());
    }

    @Test
    @Rollback
    @Transactional
    void findByUserId_shouldReturnUserLeads() {
        testData.createTestLead(testUser.getId());
        testData.createTestLead(testUser.getId());

        List<Lead> leads = leadRepository.findByUserId(testUser.getId());

        assertEquals(2, leads.size());
        leads.forEach(lead -> assertEquals(testUser.getId(), lead.getUserId()));
    }
}