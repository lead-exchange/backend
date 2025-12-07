package lead.exchange.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lead.exchange.entity.Estate;
import lead.exchange.mapper.EstateMapper;
import lead.exchange.model.EstateAttributes;
import lead.exchange.model.EstateStatus;
import lead.exchange.repository.EstateRepository;
import lead.exchange.samolet.AnalyticsplusApi;
import lead.exchange.samolet.EstatesIdByRealtyApiModel;
import lead.exchange.samolet.RealtyEstateApiModel;
import lead.exchange.samolet.TopnlabApi;
import lead.exchange.samolet.TopnlabApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class TopnlabApiServiceTest {

    @Mock
    private TopnlabApi topnlabApi;

    @Mock
    private AnalyticsplusApi analyticsplusApi;

    @Mock
    private EstateRepository estateRepository;

    @Mock
    private EstateMapper estateMapper;

    @Mock
    private RecommendationAsyncService recommendationService;

    @Captor
    private ArgumentCaptor<Estate> estateCaptor;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TopnlabApiService service;

    private final Clock fixedClock =
            Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneOffset.UTC);

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        service = new TopnlabApiService(
                topnlabApi,
                analyticsplusApi,
                estateRepository,
                estateMapper,
                objectMapper,
                recommendationService,
                fixedClock
        );
    }

    private RealtyEstateApiModel realty(long id, String commission) {
        RealtyEstateApiModel m = new RealtyEstateApiModel();
        m.setId(id);
        m.setCommissionOwnerPaysToMeValue(commission);
        return m;
    }

    private EstatesIdByRealtyApiModel analyticsIds(List<Long> ids) {
        return new EstatesIdByRealtyApiModel("71111111111", ids);
    }


    @Test
    void phoneNull_noCalls() {
        service.updateEstates(UUID.randomUUID(), null);

        verifyNoInteractions(analyticsplusApi);
        verifyNoInteractions(topnlabApi);
        verifyNoInteractions(estateRepository);
    }


    @Test
    void idsEmpty_noCalls() {
        String phone = "79991112233";

        when(analyticsplusApi.getRealtyIdsByPhone(phone))
                .thenReturn(analyticsIds(Collections.emptyList()));

        service.updateEstates(UUID.randomUUID(), phone);

        verify(analyticsplusApi).getRealtyIdsByPhone(phone);
        verifyNoInteractions(topnlabApi);
        verifyNoInteractions(estateRepository);
    }


    @Test
    void createNewEstates() {
        UUID userId = UUID.randomUUID();
        String phone = "79991112244";

        when(analyticsplusApi.getRealtyIdsByPhone(phone))
                .thenReturn(analyticsIds(List.of(100L, 200L)));

        RealtyEstateApiModel r1 = realty(100, "10");
        RealtyEstateApiModel r2 = realty(200, "20");

        when(topnlabApi.getRealtyEstateIds("100,200", null, "realty", 1))
                .thenReturn(Map.of(100L, r1, 200L, r2));

        when(estateRepository.findEstatesByExternalId(anyLong()))
                .thenReturn(Optional.empty());

        when(estateMapper.toEntity(any(RealtyEstateApiModel.class)))
                .thenReturn(new EstateAttributes());

        service.updateEstates(userId, phone);

        verify(estateRepository, times(2)).save(estateCaptor.capture());

        List<Estate> saved = estateCaptor.getAllValues();
        assertThat(saved).hasSize(2);

        assertThat(saved.get(0).getUserId()).isEqualTo(userId);
        assertThat(saved.get(0).getCommissionShare()).isEqualTo(70D);
        assertThat(saved.get(0).getStatus()).isEqualTo(EstateStatus.ACTIVE);
    }


    @Test
    void updateExistingEstate() {
        UUID userId = UUID.randomUUID();
        String phone = "79993335522";

        when(analyticsplusApi.getRealtyIdsByPhone(phone))
                .thenReturn(analyticsIds(List.of(500L)));

        RealtyEstateApiModel r = realty(500, "30");

        when(topnlabApi.getRealtyEstateIds("500", null, "realty", 1))
                .thenReturn(Map.of(500L, r));

        Estate existing = Estate.builder()
                .externalId(500L)
                .commissionShare(1D)
                .totalCommissionRate(2D)
                .build();

        when(estateRepository.findEstatesByExternalId(500L))
                .thenReturn(Optional.of(existing));

        when(estateMapper.toEntity(any(RealtyEstateApiModel.class)))
                .thenReturn(new EstateAttributes());

        service.updateEstates(userId, phone);

        verify(estateRepository).save(estateCaptor.capture());

        Estate updated = estateCaptor.getValue();

        assertThat(updated.getCommissionShare()).isEqualTo(70D);
        assertThat(updated.getTotalCommissionRate()).isEqualTo(30D);
    }


    @Test
    void topnlabEmpty_noSave() {
        String phone = "79990007766";

        when(analyticsplusApi.getRealtyIdsByPhone(phone))
                .thenReturn(analyticsIds(List.of(777L)));

        when(topnlabApi.getRealtyEstateIds("777", null, "realty", 1))
                .thenReturn(Collections.emptyMap());

        service.updateEstates(UUID.randomUUID(), phone);

        verifyNoInteractions(estateRepository);
    }


    @Test
    void topnlabThrows_noSave() {
        String phone = "79993333333";

        when(analyticsplusApi.getRealtyIdsByPhone(phone))
                .thenReturn(analyticsIds(List.of(321L)));

        when(topnlabApi.getRealtyEstateIds("321", null, "realty", 1))
                .thenThrow(new RuntimeException("fail"));

        service.updateEstates(UUID.randomUUID(), phone);

        verifyNoInteractions(estateRepository);
    }


    @Test
    void csvGeneratedCorrectly() {
        String phone = "79987776655";

        when(analyticsplusApi.getRealtyIdsByPhone(phone))
                .thenReturn(analyticsIds(List.of(1L, 2L, 3L)));

        when(topnlabApi.getRealtyEstateIds(anyString(), any(), any(), any()))
                .thenReturn(Collections.emptyMap());

        service.updateEstates(UUID.randomUUID(), phone);

        verify(topnlabApi).getRealtyEstateIds(
                eq("1,2,3"),
                any(),
                eq("realty"),
                eq(1)
        );
    }
}
