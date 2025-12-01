package lead.exchange.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ExternalRateLimiterConfig {

    @Bean
    public Bucket externalApiBucket() {
        Refill refill = Refill.intervally(1, Duration.ofSeconds(6));
        Bandwidth limit = Bandwidth.classic(1, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}

