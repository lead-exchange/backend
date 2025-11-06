package lead.exchange.repository;

import lead.exchange.entity.Lead;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.UUID;

public interface LeadRepository extends ListCrudRepository<Lead, UUID> {

    @Query("SELECT * FROM leads WHERE user_id = :userId")
    List<Lead> findByUserId(UUID userId);
}
