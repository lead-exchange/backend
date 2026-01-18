package lead.exchange.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lead.exchange.IntegrationTest;
import lead.exchange.entity.Lead;
import lead.exchange.repository.LeadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RenovationTypeDefaultTest extends IntegrationTest {

    @Autowired
    private LeadRepository leadRepository;

    @Test
    void renovationType_null_in_db_should_return_ANY() {
        Lead lead = testData.createTestLead(
                testData.createTestUser().getId()
        );

        lead.setRequirements(new Requirements()); // эмулируем старую запись в БД

        System.out.println(lead.getRequirements());
        System.out.println(lead.getRequirements().getRenovation());

        leadRepository.save(lead);

        Lead loaded = leadRepository.findById(lead.getId()).orElseThrow();

        assertEquals(
                RenovationType.ANY,
                loaded.getRequirements().getRenovation()
        );
    }
}
