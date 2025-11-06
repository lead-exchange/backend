package lead.exchange.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.Arrays;
import lead.exchange.model.EstateAttributes;
import lead.exchange.model.Requirements;
import org.postgresql.util.PGobject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.stereotype.Component;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    private static String jsonbType = "jsonb";

    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Arrays.asList(
                new RequirementsWriteConverter(),
                new RequirementsReadConverter(),
                new EstateAttributesWriteConverter(),
                new EstateAttributesReadConverter()
        ));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Component
    @WritingConverter
    public class RequirementsWriteConverter implements Converter<Requirements, PGobject> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public PGobject convert(Requirements source) {
            try {
                PGobject pgObject = new PGobject();
                pgObject.setType(jsonbType);
                pgObject.setValue(objectMapper.writeValueAsString(source));
                return pgObject;
            } catch (JsonProcessingException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Component
    @ReadingConverter
    public class RequirementsReadConverter implements Converter<PGobject, Requirements> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public Requirements convert(PGobject source) {
            try {
                return objectMapper.readValue(source.getValue(), Requirements.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @WritingConverter
    public class EstateAttributesWriteConverter implements Converter<EstateAttributes, PGobject> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public PGobject convert(EstateAttributes source) {
            try {
                PGobject pgObject = new PGobject();
                pgObject.setType(jsonbType);
                pgObject.setValue(objectMapper.writeValueAsString(source));
                return pgObject;
            } catch (JsonProcessingException | SQLException e) {
                throw new RuntimeException("Failed to convert EstateAttributes to JSON", e);
            }
        }
    }

    @ReadingConverter
    public class EstateAttributesReadConverter implements Converter<PGobject, EstateAttributes> {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public EstateAttributes convert(PGobject source) {
            try {
                return objectMapper.readValue(source.getValue(), EstateAttributes.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert JSON to EstateAttributes", e);
            }
        }
    }
}
