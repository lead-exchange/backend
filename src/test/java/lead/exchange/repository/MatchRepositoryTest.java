package lead.exchange.repository;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.entity.Match;
import lead.exchange.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MatchRepositoryTest extends IntegrationTest {

    @Autowired
    private MatchRepository matchRepository;

    @Test
    @Rollback
    @Transactional
    void save_shouldSaveMatch() {
        User user = testData.createTestUser();
        Lead lead = testData.createTestLead(user.getId());
        Estate estate = testData.createTestEstate(user.getId());

        Match match = testData.createTestMatch(lead.getId(), estate.getId());

        assertNotNull(match.getId());
        assertEquals(lead.getId(), match.getLeadId());
        assertEquals(estate.getId(), match.getEstateId());
        assertEquals("PENDING", match.getStatus());
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnMatch() {
        User user = testData.createTestUser();
        Lead lead = testData.createTestLead(user.getId());
        Estate estate = testData.createTestEstate(user.getId());
        Match match = testData.createTestMatch(lead.getId(), estate.getId());

        Optional<Match> found = matchRepository.findById(match.getId());

        assertTrue(found.isPresent());
        assertEquals(match, found.get());
    }

    @Test
    @Rollback
    @Transactional
    void findByLeadId_shouldReturnMatches() {
        User user = testData.createTestUser();
        Lead lead = testData.createTestLead(user.getId());
        Estate estate = testData.createTestEstate(user.getId());
        Match match = testData.createTestMatch(lead.getId(), estate.getId());

        List<Match> matches = matchRepository.findByLeadId(lead.getId());

        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertEquals(match, matches.getFirst());
    }

    @Test
    @Rollback
    @Transactional
    void findByEstateId_shouldReturnMatches() {
        User user = testData.createTestUser();
        Lead lead = testData.createTestLead(user.getId());
        Estate estate = testData.createTestEstate(user.getId());
        Match match = testData.createTestMatch(lead.getId(), estate.getId());

        List<Match> matches = matchRepository.findByEstateId(estate.getId());

        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertEquals(match, matches.getFirst());
    }
}