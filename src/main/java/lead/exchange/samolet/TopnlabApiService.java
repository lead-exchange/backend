package lead.exchange.samolet;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.mapper.EstateMapper;
import lead.exchange.repository.EstateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopnlabApiService {

    private final TopnlabApi topnlabApi;
    private final AnalyticsplusApi analyticsplusApi;
    public static final int TIME_SLEEP = 6000;
    public static final double DEFAULT_COMMISSION_SHARE = 70D;
    private final EstateRepository estateRepository;
    private final EstateMapper estateMapper;

    @Value("${external.api.token}")
    private String token;

    public void updateEstates(UUID userId, String phone) {
        List<Long> ids = analyticsplusApi.getRealtyEstateIds(phone).ids();

        for (Long id : ids) {
            log.info("Get estate with id " + id);
            RealtyEstateApiModel realty = topnlabApi.getRealtyEstateIds(id, token, "realty", 1);

            Estate toSave = estateRepository.findEstatesBySamoletId(realty.getId()).map(created -> {
                created.setCommissionShare(DEFAULT_COMMISSION_SHARE);
                created.setTotalCommissionRate(Double.valueOf(realty.getCommissionOwnerPaysToMeValue()));
                created.setAttributes(estateMapper.toEntity(realty));
                return created;

            }).orElseGet(() ->
                Estate.builder()
                    .userId(userId)
                    .totalCommissionRate(Double.valueOf(realty.getCommissionOwnerPaysToMeValue()))
                    .commissionShare(DEFAULT_COMMISSION_SHARE)
                    .attributes(estateMapper.toEntity(realty))
                    .samoletId(realty.getId())
                    .build());

            estateRepository.save(toSave);

            log.info("Estate with id " + id + " updated");


            try {
                Thread.sleep(TIME_SLEEP);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
