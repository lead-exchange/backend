package lead.exchange.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import io.github.bucket4j.Refill;


@Configuration
public class ExternalRateLimiterConfig {

    @Bean
    public Bucket externalApiBucket() {
        Refill refill = Refill.intervally(1, Duration.ofSeconds(6));
        Bandwidth limit = Bandwidth.classic(1, refill);
        return Bucket.builder().addLimit(limit).build();
    }
}

