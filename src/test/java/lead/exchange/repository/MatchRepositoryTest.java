package lead.exchange.repository;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.entity.Match;
import lead.exchange.entity.User;
import lead.exchange.model.MatchStatus;
import org.junit.jupiter.api.BeforeEach;
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

    private Lead testLead;
    private Estate testEstate;
    private Match testMatch;

    @BeforeEach
    void setUp() {
        User testUser = testData.createTestUser();
        testLead= testData.createTestLead(testUser.getId());
        testEstate = testData.createTestEstate(testUser.getId());
        testMatch = testData.createTestMatch(testLead.getId(), testEstate.getId());
    }

    @Test
    @Rollback
    @Transactional
    void save_shouldSaveMatch() {
        assertNotNull(testMatch.getId());
        assertEquals(testLead.getId(), testMatch.getLeadId());
        assertEquals(testEstate.getId(), testMatch.getEstateId());
        assertEquals(MatchStatus.PENDING, testMatch.getStatus());
    }

    @Test
    @Rollback
    @Transactional
    void findById_shouldReturnMatch() {
        Optional<Match> found = matchRepository.findById(testMatch.getId());

        assertTrue(found.isPresent());
        assertEquals(testMatch, found.get());
    }

    @Test
    @Rollback
    @Transactional
    void findByLeadId_shouldReturnMatches() {
        List<Match> matches = matchRepository.findByLeadId(testLead.getId());

        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertEquals(testMatch, matches.getFirst());
    }

    @Test
    @Rollback
    @Transactional
    void findByEstateId_shouldReturnMatches() {
        List<Match> matches = matchRepository.findByEstateId(testEstate.getId());

        assertFalse(matches.isEmpty());
        assertEquals(1, matches.size());
        assertEquals(testMatch, matches.getFirst());
    }
}