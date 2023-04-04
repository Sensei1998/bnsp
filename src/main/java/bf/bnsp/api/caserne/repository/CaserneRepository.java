package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.CaserneType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CaserneRepository extends JpaRepository<Caserne, Integer> {

    @Transactional
    Optional<Caserne> findByIdAndHiddenFalse(int id);

    @Transactional
    List<Caserne> findByHiddenFalse();

    @Transactional
    List<Caserne> findByCaserneTypeAndHiddenFalse(CaserneType caserneType);

    @Transactional
    List<Caserne> findByAffiliationAndHiddenFalse(Caserne caserne);
}
