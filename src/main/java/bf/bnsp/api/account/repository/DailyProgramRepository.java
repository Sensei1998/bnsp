package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.DailyProgramKeys;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailyProgramRepository extends JpaRepository<DailyProgram, DailyProgramKeys> {

    @Query("SELECT dp FROM DailyProgram dp WHERE dp.keys.caserne = ?1")
    List<DailyProgram> findDailyProgramByCaserne(Caserne caserne);
}
