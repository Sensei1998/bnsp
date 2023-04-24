package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Berickal
 */
public interface InterventionSheetRepository extends JpaRepository<InterventionSheet, InterventionFollowedKey> {

    @Query("SELECT intsh.key.caserne.id FROM InterventionSheet intsh WHERE intsh.key.intervention = ?1")
    List<Integer> findCaserneIdByIntervention(Intervention intervention);
}
