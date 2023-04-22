package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.InterventionSheetToMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface InterventionSheetToMessageRepository extends JpaRepository<InterventionSheetToMessage, Integer> {

    @Transactional
    List<InterventionSheetToMessage> findByInterventionSheet(InterventionSheet interventionSheet);

    @Transactional
    List<InterventionSheetToMessage> findByInterventionSheetAndEquipe(InterventionSheet interventionSheet, DailyTeam equipe);
}
