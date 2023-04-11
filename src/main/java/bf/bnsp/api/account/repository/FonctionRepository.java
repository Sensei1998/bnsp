package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.Fonction;
import bf.bnsp.api.account.model.FonctionKeys;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface FonctionRepository extends JpaRepository<Fonction, Integer> {

    @Query("SELECT fnc.keys.agent.id FROM Fonction fnc WHERE fnc.keys.date = ?1 AND fnc.equipe.caserne = ?2")
    List<Integer> findAgentIdByDateAndCaserne(LocalDate date, Caserne caserne);

    @Transactional
    List<Fonction> findByKeysOrderByEquipe(FonctionKeys keys);

    @Transactional
    List<Fonction> findByKeys(FonctionKeys keys);
}
