package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Affiliation;
import bf.bnsp.api.caserne.model.AffiliationRule;
import bf.bnsp.api.caserne.model.CaserneType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
public interface AffiliationRepository extends JpaRepository<Affiliation, Integer> {

    @Transactional
    Optional<Affiliation> findByDefaultStatusTrue();

    @Transactional
    Optional<Affiliation> findByIdAndHiddenFalse(int id);

    @Transactional
    List<Affiliation> findByHiddenFalse();

    @Query("SELECT afrule FROM Affiliation af JOIN af.rules afrule WHERE af.id = ?1 AND afrule.caserneType = ?2 AND af.hidden = false")
    Optional<AffiliationRule> findAffiliationRuleByAffiliationIdAndCaserneType(int id, CaserneType caserneType);
}
