package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Integer> {

    @Transactional
    Optional<Agent> findByIdAndHiddenFalse(int id);

    @Transactional
    Optional<Agent> findByCaserneAndIdAndHiddenFalse(Caserne caserne, int id);

    @Transactional
    List<Agent> findByCaserneAndHiddenFalse(Caserne caserne);

    @Transactional
    List<Agent> findByHiddenFalse();

    @Transactional
    long countByMatriculeContains(String matricule);

    @Transactional
    long countByEmailContains(String email);

    @Transactional
    Optional<Agent> findByEmailAndHiddenFalse(String email);
}
