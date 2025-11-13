package lead.exchange.service;

import java.util.*;
import lead.exchange.entity.Estate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final RestTemplate restTemplate;
    private final EstateService estateService;
    private static final String YANDEX_EMBEDDING_URL = "https://llm.api.cloud.yandex.net/foundationModels/v1/textEmbedding";
    public List<?> generateEmbedding(String text) {

        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("modelUri", "emb://b1gpnhcroeb4cb946s5j/text-search-query/latest");
            requestBody.put("text", text);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //Token Вставить сюда
            headers.set("Authorization", "Bearer ");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    YANDEX_EMBEDDING_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<?> embedding = Arrays.asList(response.getBody().get("embedding"));

                return embedding;
            }

            throw new RuntimeException("Failed to generate Yandex embedding: " + response.getStatusCode());

        } catch (Exception e) {
            log.error("Error generating Cohere embedding, using fallback method", e);
            return null;
        }
    }
    public void comparisonEstates(){
        List<Estate> estates = estateService.getAllEstate();
        Map<Estate,List<Float>> mapEstateVectorEmbedding =new HashMap<>();
        for(Estate estate:estates){
            //Иногда приходит список в списке, поэтому нужна такая проверка
            List<?> embeddingResult =generateEmbedding(estate.getAttributes().getDescription());
            if (embeddingResult.getFirst() instanceof List) {
                List<Float> vector = (List<Float>) embeddingResult.get(0);
                mapEstateVectorEmbedding.put(estate, vector);
            } else {
                mapEstateVectorEmbedding.put(estate, (List<Float>) embeddingResult);
            }

        }
        for( int i=0;i<estates.size();i++){
            for (int j = i+1; j < estates.size(); j++) {
                System.out.println(estates.get(i).getId()+"  "+estates.get(j).getId()+"  "+calculateCosineSimilarity((List<Float>) mapEstateVectorEmbedding.get(estates.get(i)), (List<Float>) mapEstateVectorEmbedding.get(estates.get(j))));
            }
        }
    }


    public double calculateCosineSimilarity(List<? extends Number> vector1, List<? extends Number> vector2) {
        if (vector1 == null || vector2 == null || vector1.size() != vector2.size()) {
            return 0.0;
        }

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.size(); i++) {
            double val1 = vector1.get(i).doubleValue();
            double val2 = vector2.get(i).doubleValue();

            dotProduct += val1 * val2;
            norm1 += Math.pow(val1, 2);
            norm2 += Math.pow(val2, 2);
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }

        double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        return Math.max(0.0, Math.min(1.0, similarity));
    }
}