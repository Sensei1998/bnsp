package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
public interface EnginRepository extends JpaRepository<Engin, Integer> {

    @Transactional
    Optional<Engin> findByIdAndHiddenFalse(int enginId);

    @Transactional
    Optional<Engin> findByIdAndCaserneAndHiddenFalse(int enginId, Caserne caserne);

    @Transactional
    List<Engin> findByCaserneAndHiddenFalse(Caserne caserne);

    @Transactional
    List<Engin> findByHiddenFalse();

    @Transactional
    Optional<Engin> findByImmatriculationAndHiddenFalse(String immatriculation);

    @Transactional
    long countByImmatriculationContains(String immatriculation);

}
