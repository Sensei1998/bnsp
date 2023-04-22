package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Affiliation;
import bf.bnsp.api.caserne.model.AffiliationLink;
import bf.bnsp.api.caserne.model.AffiliationRule;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AffiliationLinkRepository extends JpaRepository<AffiliationLink, Integer> {

    @Transactional
    Optional<AffiliationLink> findByAffiliationTypeAndCaserneChild(Affiliation affiliation, Caserne child);

    @Transactional
    List<AffiliationLink> findByAffiliationTypeAndCaserneParent(Affiliation affiliation, Caserne caserne);
}
