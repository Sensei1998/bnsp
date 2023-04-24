package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.Sinister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Berickal
 */
public interface SinisterRepository extends JpaRepository<Sinister, Integer> {

    @Transactional
    List<Sinister> findByInterventionSheet(InterventionSheet interventionSheet);

    @Query("SELECT sin FROM Sinister sin WHERE sin.interventionSheet.key.intervention = ?1")
    List<Sinister> findSinisterByIntervention(Intervention intervention);

}
