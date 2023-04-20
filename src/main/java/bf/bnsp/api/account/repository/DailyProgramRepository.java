package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

public interface DailyProgramRepository extends JpaRepository<DailyProgram, Long> {

    @Transactional
    Optional<DailyProgram> findByIdAndHiddenFalse(long id);

    @Transactional
    Optional<DailyProgram> findByDateAndCaserneAndHiddenFalse(LocalDate date, Caserne caserne);
}
