package lead.exchange.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import lead.exchange.entity.Recommendation;

public interface RecommendationsRepository extends ListCrudRepository<Recommendation, UUID>{
    @Query("SELECT * FROM recommendations WHERE source_id = :id ORDER BY similarity_score DESC LIMIT 10")
    List<Recommendation> getListForLead(UUID id);

    @Query("SELECT * FROM recommendations WHERE target_id = :id ORDER BY similarity_score DESC LIMIT 10")
    List<Recommendation> getListForEstate(UUID id);
}
