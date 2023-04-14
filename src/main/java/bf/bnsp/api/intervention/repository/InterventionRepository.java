package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterventionRepository extends JpaRepository<Intervention, Integer> {
}
