package lead.exchange.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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

}
