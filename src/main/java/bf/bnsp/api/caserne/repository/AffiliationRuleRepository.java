package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Affiliation;
import bf.bnsp.api.caserne.model.AffiliationRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationRuleRepository extends JpaRepository<AffiliationRule, Integer> {
}
