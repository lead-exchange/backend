package lead.exchange;

import lead.exchange.entity.Estate;
import lead.exchange.entity.Lead;
import lead.exchange.entity.Match;
import lead.exchange.entity.User;
import lead.exchange.model.EstateAttributes;
import lead.exchange.model.EstateStatus;
import lead.exchange.model.LeadStatus;
import lead.exchange.model.MatchStatus;
import lead.exchange.model.Requirements;
import lead.exchange.repository.EstateRepository;
import lead.exchange.repository.LeadRepository;
import lead.exchange.repository.MatchRepository;
import lead.exchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class TestDataHelper {

    @Autowired
    private Clock clock;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private EstateRepository estateRepository;

    @Autowired
    private MatchRepository matchRepository;

    public User createTestUser() {
        LocalDateTime now = LocalDateTime.now(clock);
        return userRepository.save(
                User.builder()
                        .telegramId("test_user_" + UUID.randomUUID())
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
    }

    public User createTestUserWithTelegramId(String telegramId) {
        LocalDateTime now = LocalDateTime.now(clock);
        return userRepository.save(
                User.builder()
                        .telegramId(telegramId)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
    }

    public Lead createTestLead(UUID userId) {
        Requirements requirements = Requirements.builder()
                .propertyType("APARTMENT")
                .minPrice(100000.0)
                .maxPrice(200000.0)
                .minArea(50)
                .maxArea(100)
                .bedrooms(2)
                .locations(List.of("City Center"))
                .build();

        LocalDateTime now = LocalDateTime.now(clock);

        return leadRepository.save(
                Lead.builder()
                        .userId(userId)
                        .requirements(requirements)
                        .status(LeadStatus.ACTIVE)
                        .commissionShare(50.0)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
    }

    public Estate createTestEstate(UUID userId) {
        EstateAttributes attributes = EstateAttributes.builder()
                .title("Beautiful Apartment")
                .description("Spacious apartment in city center")
                .address("123 Main St")
                .price(150000.0)
                .area(75)
                .bedrooms(2)
                .photos(List.of("photo1.jpg", "photo2.jpg"))
                .build();

        LocalDateTime now = LocalDateTime.now(clock);

        return estateRepository.save(
                Estate.builder()
                        .userId(userId)
                        .attributes(attributes)
                        .totalCommissionRate(5.0)
                        .commissionShare(50.0)
                        .status(EstateStatus.ACTIVE)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );
    }

    public Match createTestMatch(UUID leadId, UUID estateId) {
        LocalDateTime now = LocalDateTime.now(clock);

        return matchRepository.save(
                Match.builder()
                        .leadId(leadId)
                        .estateId(estateId)
                        .status(MatchStatus.PENDING)
                        .matchedAt(now)
                        .build()
        );
    }
}