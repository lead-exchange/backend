package lead.exchange.repository;

import lead.exchange.entity.Match;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.UUID;

public interface MatchRepository extends ListCrudRepository<Match, UUID> {

    @Query("SELECT * FROM matches WHERE lead_id = :leadId")
    List<Match> findByLeadId(UUID leadId);

    @Query("SELECT * FROM matches WHERE estate_id = :estateId")
    List<Match> findByEstateId(UUID estateId);
}
