package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterventionSheetRepository extends JpaRepository<InterventionSheet, InterventionFollowedKey> {
}
