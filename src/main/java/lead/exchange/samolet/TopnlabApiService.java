package lead.exchange.samolet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
            log.info("Skipping update: phone is null for user {}", userId);
            return;
        }

        List<Long> ids = analyticsplusApi.getRealtyIdsByPhone(phone).ids();
        if (ids == null || ids.isEmpty()) {
            log.info("No external ids returned for phone {} (user {})", phone, userId);
            return;
        }

        LocalDateTime now = LocalDateTime.now(clock);

        int batchSize = 10;
        List<List<Long>> batches = new ArrayList<>();

        for (int i = 0; i < ids.size(); i += batchSize) {
            batches.add(ids.subList(i, Math.min(i + batchSize, ids.size())));
        }

        log.info("Processing {} ids in {} batches (batch size={})", ids.size(), batches.size(), batchSize);

        for (List<Long> batch : batches) {
            String idsStr = batch.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            try {
                log.info("Batch request for ids: {}", idsStr);

                Map<Object, RealtyEstateApiModel> response = topnlabApi.getRealtyEstateIds(idsStr, token, "realty", 1);

                if (response == null || response.isEmpty()) {
                    log.info("Topnlab returned empty response for batch {}", idsStr);
                    continue;
                }

                for (RealtyEstateApiModel realty : response.values()) {
                    try {
                        Estate toSave = estateRepository.findEstatesByExternalId(realty.getId())
                                .map(existing -> {
                                    existing.setCommissionShare(DEFAULT_COMMISSION_SHARE);
                                    existing.setTotalCommissionRate(
                                            realty.getCommissionOwnerPaysToMeValue() != null
                                                    ? Double.valueOf(realty.getCommissionOwnerPaysToMeValue())
                                                    : null
                                    );
                                    existing.setAttributes(estateMapper.toEntity(realty));
                                    existing.setUpdatedAt(now);
                                    return existing;
                                })
                                .orElseGet(() ->
                                    Estate.builder()
                                        .userId(userId)
                                        .totalCommissionRate(
                                            realty.getCommissionOwnerPaysToMeValue() != null
                                                ? Double.valueOf(realty.getCommissionOwnerPaysToMeValue())
                                                : null
                                        )
                                        .commissionShare(DEFAULT_COMMISSION_SHARE)
                                        .attributes(estateMapper.toEntity(realty))
                                        .externalId(realty.getId())
                                        .status(EstateStatus.ACTIVE)
                                        .createdAt(now)
                                        .updatedAt(now)
                                        .build()
                                );

                        estateRepository.save(toSave);
                        log.info("Estate with external id {} processed", realty.getId());
                    } catch (Exception e) {
                        log.error("Failed to import realty id={} for user {} : {}",
                                realty != null ? realty.getId() : "null", userId, e.getMessage(), e);
                    }
                }

            } catch (Exception e) {
                log.error("Failed batch for ids={} for user {} : {}", idsStr, userId, e.getMessage(), e);
            }
        }

        log.info("Update is finished for user {}", userId);

    }


}
