package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.InterventionSheetToTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Berickal
 */
public interface InterventionSheetToTeamRepository extends JpaRepository<InterventionSheetToTeam, Integer> {

    @Transactional
    Optional<InterventionSheetToTeam> findByInterventionSheetAndEquipe(InterventionSheet interventionSheet, DailyTeam equipe);
}
