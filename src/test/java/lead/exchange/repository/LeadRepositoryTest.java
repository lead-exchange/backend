package lead.exchange.repository;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.Lead;
import lead.exchange.entity.User;
import lead.exchange.model.EstateStatus;
import lead.exchange.model.LeadStatus;
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

    @Test
    @Rollback
    @Transactional
    void save_shouldSaveLeadWithRequirements() {
        User user = testData.createTestUser();
        Lead lead = testData.createTestLead(user.getId());

        assertNotNull(lead.getId());
        assertEquals(user.getId(), lead.getUserId());
        assertEquals(LeadStatus.ACTIVE, lead.getStatus());
        assertNotNull(lead.getRequirements());
        assertEquals("APARTMENT", lead.getRequirements().getPropertyType());
        assertEquals(100000.0, lead.getRequirements().getMinPrice());
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnLead() {
        User user = testData.createTestUser();
        Lead lead = testData.createTestLead(user.getId());

        Optional<Lead> found = leadRepository.findById(lead.getId());

        assertTrue(found.isPresent());
        assertEquals(lead, found.get());
    }

    @Test
    @Rollback
    @Transactional
    void findByUserId_shouldReturnUserLeads() {
        User user = testData.createTestUser();
        testData.createTestLead(user.getId());
        testData.createTestLead(user.getId());

        List<Lead> leads = leadRepository.findByUserId(user.getId());

        assertEquals(2, leads.size());
        leads.forEach(lead -> assertEquals(user.getId(), lead.getUserId()));
    }
}