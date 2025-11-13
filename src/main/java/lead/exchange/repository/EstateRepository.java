package lead.exchange.repository;

import java.util.List;
import java.util.UUID;
import lead.exchange.entity.Estate;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface EstateRepository extends ListCrudRepository<Estate, UUID> {

    @Query("SELECT * FROM estates WHERE user_id = :userId")
    List<Estate> findByUserId(UUID userId);

    @Query("SELECT * FROM estates ")
    List<Estate> findAll();
}
