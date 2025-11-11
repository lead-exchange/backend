package lead.exchange.repository;


import lead.exchange.entity.MatchLog;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchLogRepository extends ListCrudRepository<MatchLog, UUID> {

    @Query("SELECT * FROM matches_log WHERE match_id = :matchId ORDER BY created_at DESC")
    List<MatchLog> findByMatchId(UUID matchId);

}