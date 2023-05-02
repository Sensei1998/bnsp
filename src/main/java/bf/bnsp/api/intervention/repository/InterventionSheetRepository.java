package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.caserne.model.Caserne;
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

    @Query("SELECT intsh FROM InterventionSheet intsh WHERE intsh.key.caserne = ?1 AND intsh.hidden = false")
    List<InterventionSheet> findInterventionSheetByCaserne(Caserne caserne);

    @Query("SELECT intsh FROM InterventionSheet intsh WHERE intsh.key.intervention = ?1 AND intsh.hidden = false")
    List<InterventionSheet> findInterventionSheetByIntervention(Intervention intervention);

}
