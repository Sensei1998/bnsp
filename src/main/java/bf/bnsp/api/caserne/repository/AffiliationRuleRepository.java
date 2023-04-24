package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Affiliation;
import bf.bnsp.api.caserne.model.AffiliationRule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Berickal
 */
public interface AffiliationRuleRepository extends JpaRepository<AffiliationRule, Integer> {
}
