package lead.exchange;

import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.entity.Match;
import lead.exchange.entity.User;
import lead.exchange.model.EstateAttributes;
import lead.exchange.model.Requirements;
import lead.exchange.repository.EstateRepository;
import lead.exchange.repository.LeadRepository;
import lead.exchange.repository.MatchRepository;
import lead.exchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TestDataHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private EstateRepository estateRepository;

    @Autowired
    private MatchRepository matchRepository;

    public User createTestUser() {
        User user = new User();
        user.setTelegramId("test_user_" + UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPhone("+1234567890");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User createTestUserWithTelegramId(String telegramId) {
        User user = new User();
        user.setTelegramId(telegramId);
        user.setEmail("test@example.com");
        user.setPhone("+1234567890");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Lead createTestLead(UUID userId) {
        Requirements requirements = new Requirements();
        requirements.setPropertyType("APARTMENT");
        requirements.setMinPrice(100000.0);
        requirements.setMaxPrice(200000.0);
        requirements.setMinArea(50);
        requirements.setMaxArea(100);
        requirements.setBedrooms(2);
        requirements.setLocations(List.of("City Center"));

        Lead lead = new Lead();
        lead.setUserId(userId);
        lead.setRequirements(requirements);
        lead.setStatus("ACTIVE");
        lead.setCommissionShare(50.0);
        lead.setCreatedAt(LocalDateTime.now());
        lead.setUpdatedAt(LocalDateTime.now());
        return leadRepository.save(lead);
    }

    public Estate createTestEstate(UUID userId) {
        EstateAttributes attributes = new EstateAttributes();
        attributes.setTitle("Beautiful Apartment");
        attributes.setDescription("Spacious apartment in city center");
        attributes.setAddress("123 Main St");
        attributes.setPrice(150000.0);
        attributes.setArea(75);
        attributes.setBedrooms(2);
        attributes.setPhotos(List.of("photo1.jpg", "photo2.jpg"));

        Estate estate = new Estate();
        estate.setUserId(userId);
        estate.setAttributes(attributes);
        estate.setTotalCommissionRate(5.0);
        estate.setCommissionShare(50.0);
        estate.setStatus("AVAILABLE");
        estate.setCreatedAt(LocalDateTime.now());
        estate.setUpdatedAt(LocalDateTime.now());
        return estateRepository.save(estate);
    }

    public Match createTestMatch(UUID leadId, UUID estateId) {
        Match match = new Match();
        match.setLeadId(leadId);
        match.setEstateId(estateId);
        match.setStatus("PENDING");
        match.setMatchedAt(LocalDateTime.now());
        return matchRepository.save(match);
    }
}