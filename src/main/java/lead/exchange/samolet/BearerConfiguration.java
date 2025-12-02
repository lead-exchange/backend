package lead.exchange.samolet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BearerConfiguration {

    private final String apiKey;

    public BearerConfiguration(@Value("${external.api.token}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Bean
    public ApiKeyInterceptor interceptor() {
        return new ApiKeyInterceptor(apiKey);
    }

}
