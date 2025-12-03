package lead.exchange.samolet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.mapper.EstateMapper;
import lead.exchange.model.EstateStatus;
import lead.exchange.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopnlabApiService {

    public static final double DEFAULT_COMMISSION_SHARE = 70D;

    private final TopnlabApi topnlabApi;
    private final AnalyticsplusApi analyticsplusApi;
    private final EstateRepository estateRepository;
    private final EstateMapper estateMapper;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Value("${external.api.token}")
    private String token;

    public void updateEstates(UUID userId, String phone) {
        if (phone == null) {
            log.info("Skipping update");
        }
        List<Long> ids = analyticsplusApi.getRealtyIdsByPhone(phone).ids();

        for (Long id : ids) {
            try {
                LocalDateTime now = LocalDateTime.now(clock);
                log.info("Get estate with id " + id);
                RealtyEstateApiModel realty = topnlabApi.getRealtyEstateIds(id, token, "realty", 1)
                    .values()
                    .stream()
                    .toList()
                    .getFirst();

                Estate toSave = estateRepository.findEstatesByExternalId(realty.getId()).map(created -> {
                    created.setCommissionShare(DEFAULT_COMMISSION_SHARE);
                    created.setTotalCommissionRate(realty.getCommissionOwnerPaysToMeValue() != null
                        ? Double.valueOf(realty.getCommissionOwnerPaysToMeValue())
                        : null);
                    created.setAttributes(estateMapper.toEntity(realty));
                    created.setUpdatedAt(now);
                    return created;

                }).orElseGet(() ->
                    Estate.builder()
                        .userId(userId)
                        .totalCommissionRate(realty.getCommissionOwnerPaysToMeValue() != null
                            ? Double.valueOf(realty.getCommissionOwnerPaysToMeValue())
                            : null)
                        .commissionShare(DEFAULT_COMMISSION_SHARE)
                        .attributes(estateMapper.toEntity(realty))
                        .externalId(realty.getId())
                        .status(EstateStatus.ACTIVE)
                        .createdAt(now)
                        .updatedAt(now)
                        .build());

                estateRepository.save(toSave);

                log.info("Estate with id " + id + " updated");

            } catch (Exception e) {
                log.error("Failed to import id={} {}", id, e.getMessage());
                log.error(Arrays.toString(e.getStackTrace()));
            }

        }
        log.info("Update is finished for user {}", userId);

    }


}
