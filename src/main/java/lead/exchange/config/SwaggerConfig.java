package lead.exchange.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "lead exchange",
        version = "v1",
        description = """
            branch: ${git.branch}
            commit message: ${git.commit.message.full}
            commit user: ${git.commit.user.name}
            commit hash: ${git.commit.id}
            commit time: ${git.commit.time}
            build time: ${git.build.time}
            """
    )
)
public class SwaggerConfig {
    @Bean
    public OpenAPI openApi() {
        String tmaAuthStr = "tmaAuth";
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(tmaAuthStr,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("Введите целиком: tma {initData}")))
                .addSecurityItem(new SecurityRequirement().addList(tmaAuthStr));
    }
}
