package lead.exchange.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lead.exchange.entity.Match;
import lead.exchange.entity.MatchUpdateEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface MatchRepository extends ListCrudRepository<Match, UUID> {

    @Query("SELECT * FROM matches WHERE lead_id = :leadId")
    List<Match> findByLeadId(UUID leadId);

    @Query("SELECT * FROM matches WHERE estate_id = :estateId")
    List<Match> findByEstateId(UUID estateId);

    @Query("SELECT * FROM matches WHERE estate_id = :estateId and lead_id =:leadId ")
    Optional<Match> findByEstateIdAndLeadId(UUID estateId, UUID leadId);

    @Query("""
        UPDATE matches SET
            lead_commission = :#{#matchDto.leadCommission},
            updated_by = :#{#matchDto.updatedBy},
            comment = :#{#matchDto.comment},
            lead_status = :#{#matchDto.status.toString()},
            updated_at = :#{#matchDto.updatedAt}
        WHERE id = :#{#matchDto.id}
        """
    )
    @Modifying
    void updateLeadMatch(MatchUpdateEntity matchDto);

    @Query("""
        UPDATE matches SET
            lead_commission = :#{#matchDto.leadCommission},
            updated_by = :#{#matchDto.updatedBy},
            comment = :#{#matchDto.comment},
            estate_status = :#{#matchDto.status.toString()},
            updated_at = :#{#matchDto.updatedAt}
        WHERE id = :#{#matchDto.id}
        """
    )
    @Modifying
    void updateEstateMatch(MatchUpdateEntity matchDto);

}
