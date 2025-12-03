package lead.exchange.repository;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Recommendation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface RecommendationsRepository extends ListCrudRepository<Recommendation, UUID> {
    @Query("""
        SELECT r.*
        FROM recommendations r
        JOIN matches m
            ON m.lead_id = r.source_id
           AND m.estate_id = r.target_id
        WHERE r.source_id = :id
          AND m.lead_status = '0'
        ORDER BY r.similarity_score DESC
        LIMIT :limit
    """)
    List<Recommendation> getListForLead(UUID id, int limit);

    @Query("""
        SELECT r.*
        FROM recommendations r
        JOIN matches m
            ON m.lead_id = r.source_id
           AND m.estate_id = r.target_id
        WHERE r.target_id = :id
          AND m.estate_status = '0'
        ORDER BY r.similarity_score DESC
        LIMIT :limit
    """)
    List<Recommendation> getListForEstate(UUID id, int limit);
}
