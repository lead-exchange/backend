package lead.exchange.repository;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Recommendation;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RecommendationsRepository extends ListCrudRepository<Recommendation, UUID> {
    @Query("""
        SELECT r.*
        FROM recommendations r
        LEFT JOIN matches m
            ON m.lead_id = r.source_id
           AND m.estate_id = r.target_id
        WHERE r.source_id = :id
          AND (m.lead_status = '0' OR m.lead_id IS NULL)
        ORDER BY r.similarity_score, r.target_id DESC
        LIMIT :limit
    """)
    List<Recommendation> getListForLead(UUID id, int limit);

    @Query("""
        SELECT r.*
        FROM recommendations r
        LEFT JOIN matches m
            ON m.lead_id = r.source_id
           AND m.estate_id = r.target_id
        WHERE r.source_id = :id
          AND (m.lead_status = '0' OR m.lead_id IS NULL)
          AND (
            r.similarity_score < :lastScore
            OR (r.similarity_score = :lastScore AND r.target_id > :lastUuid)
              )
        ORDER BY r.similarity_score, r.target_id DESC
        LIMIT :limit
    """)
    List<Recommendation> getListForLeadWithPaging(UUID id, int limit, double lastScore, UUID lastUuid);

    @Query("""
        SELECT r.*
        FROM recommendations r
        LEFT JOIN matches m
            ON m.lead_id = r.source_id
           AND m.estate_id = r.target_id
        WHERE r.target_id = :id
          AND (m.estate_status = '0' OR m.estate_id is NULL)
        ORDER BY r.similarity_score, r.source_id DESC
        LIMIT :limit
    """)
    List<Recommendation> getListForEstate(UUID id, int limit);

    @Query("""
        SELECT r.*
        FROM recommendations r
        LEFT JOIN matches m
            ON m.lead_id = r.source_id
           AND m.estate_id = r.target_id
        WHERE r.target_id = :id
          AND (m.lead_status = '0' OR m.lead_id IS NULL)
          AND (
            r.similarity_score < :lastScore
            OR (r.similarity_score = :lastScore AND r.source_id > :lastUuid)
              )
        ORDER BY r.similarity_score, r.source_id DESC
        LIMIT :limit
    """)
    List<Recommendation> getListForEstateWithPaging(UUID id, int limit, double lastScore, UUID lastUuid);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM recommendations
    WHERE target_id = :targetId
""")
    void deleteAllByEstate(UUID targetId);

    @Modifying
    @Transactional
    @Query("""
    DELETE FROM recommendations
    WHERE source_id = :source_id
""")
    void deleteAllByLead(UUID sourceId);
}
