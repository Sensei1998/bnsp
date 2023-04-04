package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Integer> {
}
