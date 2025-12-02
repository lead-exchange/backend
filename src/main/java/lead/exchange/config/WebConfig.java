package lead.exchange.config;

import java.util.List;
import lead.exchange.security.CurrentUserArgumentResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // разрешаем все пути
                        .allowedOrigins(
                                "http://localhost:5173",
                                "https://lead-exchange.github.io"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true);
            }

            @Override
            public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(new CurrentUserArgumentResolver());
            }
        };
    }
}
