package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.model.EquipeType;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface EquipeRepository extends JpaRepository<Equipe, Integer> {

    @Transactional
    Optional<Equipe> findByIdAndHiddenFalse(int id);

    @Transactional
    List<Equipe> findByHiddenFalse();

    @Transactional
    List<Equipe> findByCaserneAndHiddenFalse(Caserne caserne);

    @Transactional
    List<Equipe> findByCaserneAndEquipeTypeAndHiddenFalse(Caserne caserne, EquipeType equipeType);

}
