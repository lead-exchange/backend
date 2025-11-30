package lead.exchange.external;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lead.exchange.config.ExternalApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalEstateClient {
    private final ExternalApiProperties props;
    private WebClient externalWebClient;

    @PostConstruct
    public void init() {
        this.externalWebClient = WebClient.builder()
                .defaultHeader("Authorization", "Bearer " + props.getToken())
                .build();
    }

    public List<String> getIdsByPhone(String phone) {
        String url = props.getUrlIds() + "?phone=" + phone;

        JsonNode response = externalWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String idsFiels = "ids";

        if (response == null || !response.has(idsFiels)) {
            log.warn("Don't find any estates for phone = {}", phone);
            return List.of();
        }

        List<String> ids = new ArrayList<>();
        response.get(idsFiels).forEach(id -> ids.add(id.asText()));

        return ids;
    }

    public JsonNode getEntityById(String id) {
        String url = props.getUrlEntity() + "?id=" + id + "&key=" + props.getToken() + "&type=realty&short=1";

        return externalWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }
}
