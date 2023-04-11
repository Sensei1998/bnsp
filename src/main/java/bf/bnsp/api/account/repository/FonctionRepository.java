package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.Fonction;
import bf.bnsp.api.account.model.FonctionKeys;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FonctionRepository extends JpaRepository<Fonction, Integer> {

    @Query("SELECT fnc.keys.agent.id FROM Fonction fnc WHERE fnc.keys.date = ?1 AND fnc.equipe.caserne = ?2")
    List<Integer> findAgentIdByDateAndCaserne(LocalDate date, Caserne caserne);

    @Query("SELECT fnc FROM Fonction fnc WHERE fnc.equipe.caserne = ?1 ORDER BY fnc.keys.date")
    List<Fonction> findFonctionByCaserneOrderByDate(Caserne caserne);

    @Query("SELECT fnc.keys.date FROM Fonction fnc WHERE fnc.equipe.caserne = ?1 ORDER BY fnc.keys.date")
    List<LocalDate> findDailyDateByCaserneOrderByDate(Caserne caserne);

    @Transactional
    Optional<Fonction> findByKeys(FonctionKeys keys);

    @Query("SELECT fnc FROM Fonction fnc WHERE fnc.keys.date = ?1 AND fnc.equipe.caserne = ?2 ORDER BY fnc.equipe.id")
    List<Fonction> findFonctionIdByDateAndCaserneOrderByEquipe(LocalDate date, Caserne caserne);
}
