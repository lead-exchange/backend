package lead.exchange.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lead.exchange.security.TelegramAuthFilter;
import lead.exchange.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Value("${tma.auth.enabled:true}")
    private boolean enabled;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${tma.auth.max-age-seconds:300}")
    private long maxAgeSeconds;

    @Value("${tma.auth.public-paths:/swagger-ui,/v3/api-docs}")
    private String publicPathsProp;

    @Value("${enable.local.dev}")
    private Boolean isEnabledLocalDev;

    @Bean
    public FilterRegistrationBean<TelegramAuthFilter> telegramAuthFilter(
        AuthService authService,
        ObjectMapper objectMapper
    ) {
        List<String> publicPaths = Arrays.stream(publicPathsProp.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        TelegramAuthFilter filter = new TelegramAuthFilter(
                enabled,
                botToken,
                maxAgeSeconds,
                publicPaths,
            authService,
            objectMapper,
            isEnabledLocalDev
        );

        FilterRegistrationBean<TelegramAuthFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(filter);
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        return bean;
    }
}
