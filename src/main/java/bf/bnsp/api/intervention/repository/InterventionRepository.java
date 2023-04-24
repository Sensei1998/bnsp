package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Berickal
 */
public interface InterventionRepository extends JpaRepository<Intervention, Integer> {
}
