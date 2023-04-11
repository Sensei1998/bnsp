package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.DailyProgramCreationForm;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.model.Fonction;
import bf.bnsp.api.caserne.model.Caserne;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface FonctionServiceInterface {

    List<Fonction> createDailyProgram(DailyProgramCreationForm dailyProgramForm);

    List<Fonction> updateDailyProgram(DailyProgramCreationForm dailyProgramForm, LocalDate date, Caserne caserne);

    List<Fonction> findDailyProgramDetailByCaserneAndDate(Caserne caserne, LocalDate date);

    List<Fonction> findDailyProgramByCaserneAndDate(Caserne caserne, LocalDate date);

    List<Fonction> findAllDailyProgram();

    List<Fonction> findAllDailyProgramByCaserne(Caserne caserne);

    List<Fonction> deleteDailyProgramByCaserneAndDate(Caserne caserne, Date date);
}
