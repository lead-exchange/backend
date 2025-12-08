package lead.exchange.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lead.exchange.entity.Match;
import lead.exchange.entity.MatchUpdateEntity;
import lead.exchange.entity.MatchWithEstate;
import lead.exchange.entity.MatchWithLead;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface MatchRepository extends ListCrudRepository<Match, UUID> {

    @Query("SELECT matches.*, estates.attributes->>'title' as estate_title, "
        + "ARRAY (select jsonb_array_elements_text(estates.attributes->'photos')) as estate_photos "
        + "FROM matches JOIN estates on estates.id = estate_id WHERE lead_id = :leadId")
    List<MatchWithEstate> findByLeadId(UUID leadId);

    @Query("SELECT matches.*, leads.name as lead_name FROM matches "
        + "JOIN leads on lead_id=leads.id WHERE estate_id = :estateId")
    List<MatchWithLead> findByEstateId(UUID estateId);

    @Query("SELECT * FROM matches WHERE estate_id = :estateId and lead_id =:leadId ")
    Optional<Match> findByEstateIdAndLeadId(UUID estateId, UUID leadId);

    @Query("""
        UPDATE matches SET
            lead_commission = :#{#matchDto.leadCommission},
            updated_by = :#{#matchDto.updatedBy},
            comment = :#{#matchDto.comment},
            lead_status = :#{#matchDto.status.toString()},
            updated_at = :#{#matchDto.updatedAt},
            common_status = :#{#matchDto.commonStatus.toString()}
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
            updated_at = :#{#matchDto.updatedAt},
            common_status = :#{#matchDto.commonStatus.toString()}
        WHERE id = :#{#matchDto.id}
        """
    )
    @Modifying
    void updateEstateMatch(MatchUpdateEntity matchDto);

}
