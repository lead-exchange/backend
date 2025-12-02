package lead.exchange.samolet;


import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ApiKeyInterceptor implements RequestInterceptor {
    private final String apiKey;

    public ApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + apiKey);
    }
}

