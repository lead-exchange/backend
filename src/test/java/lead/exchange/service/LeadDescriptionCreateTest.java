package lead.exchange.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import lead.exchange.IntegrationTest;
import lead.exchange.dto.LeadCreateDto;
import lead.exchange.entity.Lead;
import lead.exchange.model.Requirements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LeadDescriptionCreateTest extends IntegrationTest {

    @Autowired
    private LeadService leadService;

    @Test
    void createLead_should_save_description_from_requirements() {
        Requirements requirements = Requirements.builder()
                .propertyType("APARTMENT")
                .description("Очень важное описание лида")
                .build();

        LeadCreateDto dto = new LeadCreateDto(
                "Test Lead",
                requirements,
                5.0
        );

        Lead lead = leadService.createLead(
                dto,
                testData.createTestUser().getId()
        );

        assertNotNull(lead.getRequirements());
        assertEquals(
                "Очень важное описание лида",
                lead.getRequirements().getDescription()
        );
    }
}
