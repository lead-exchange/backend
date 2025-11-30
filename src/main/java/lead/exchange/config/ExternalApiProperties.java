package lead.exchange.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.api")
public class ExternalApiProperties {
    private String token;
    private String urlIds;
    private String urlEntity;
}
