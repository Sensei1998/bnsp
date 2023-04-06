package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.DailyProgramCreationForm;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.caserne.model.Caserne;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DailyProgramServiceInterface {

    Optional<DailyProgram> createDailyProgram(DailyProgramCreationForm dailyProgramForm, Caserne caserne);

    Optional<DailyProgram> updateDailyProgram(DailyProgramCreationForm dailyProgramForm, Caserne caserne, DailyProgram targetedProgram);

    Optional<DailyProgram> findDailyProgramByCaserneAndDate(Caserne caserne, LocalDate date);

    List<DailyProgram> findAllDailyProgram();

    List<DailyProgram> findAllDailyProgramByCaserne(Caserne caserne);

    Optional<DailyProgram> deleteDailyProgramByCaserneAndDate(Caserne caserne, Date date);
}
