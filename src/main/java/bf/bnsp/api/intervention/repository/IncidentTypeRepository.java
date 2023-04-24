package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.IncidentType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Berickal
 */
public interface IncidentTypeRepository extends JpaRepository<IncidentType, Integer> {
}
