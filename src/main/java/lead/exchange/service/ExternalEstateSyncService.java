package lead.exchange.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.external.ExternalEstateClient;
import lead.exchange.external.ExternalEstateMapper;
import lead.exchange.model.EstateAttributes;
import lead.exchange.model.EstateStatus;
import lead.exchange.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalEstateSyncService {

    private final UserService userService;
    private final EstateRepository estateRepository;
    private final ExternalEstateClient client;
    private final ExternalEstateMapper mapper;
    private final Clock clock;

    public Map<String, Object> syncUser(UUID userId) {
        String phone = userService.getUserPhone(userId);

        if (phone == null) {
            throw new IllegalArgumentException("User has no phone number");
        }

        List<String> ids = client.getIdsByPhone(phone);
        List<String> created = new ArrayList<>();
        List<String> updated = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (String id : ids) {
            try {
                JsonNode json = client.getEntityById(id);
                JsonNode entity = json.get(id);
                if (entity == null) {
                    failed.add(id);
                    continue;
                }

                EstateAttributes attributes = mapper.toAttributes(entity);

                Optional<Estate> existing =
                        estateRepository.findByUserId(userId).stream()
                                .filter(e -> id.equals(e.getExternalId()))
                                .findFirst();

                LocalDateTime now = LocalDateTime.now(clock);

                if (existing.isPresent()) {
                    Estate estate = existing.get();
                    estate.setAttributes(attributes);
                    estate.setUpdatedAt(now);
                    estateRepository.save(estate);
                    updated.add(id);
                } else {
                    Estate estate = Estate.builder()
                            .userId(userId)
                            .externalId(id)
                            .attributes(attributes)
                            .createdAt(now)
                            .updatedAt(now)
                            .status(EstateStatus.ACTIVE)
                            .build();
                    estateRepository.save(estate);
                    created.add(id);
                }
            } catch (Exception ex) {
                log.error("Failed to import id={} {}", id, ex.getMessage());
                failed.add(id);
            }
        }

        return Map.of(
                "created", created,
                "updated", updated,
                "failed", failed
        );
    }
}
