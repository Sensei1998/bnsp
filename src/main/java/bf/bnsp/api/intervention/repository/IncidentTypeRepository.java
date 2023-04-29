package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.CategoryIncident;
import bf.bnsp.api.intervention.model.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Berickal
 */
public interface IncidentTypeRepository extends JpaRepository<IncidentType, Integer> {

    @Query("SELECT ci FROM CategoryIncident ci JOIN ci.types it WHERE it.id = ?1")
    Optional<CategoryIncident> findCategoryIncidentByIncidentType(int id);
}
