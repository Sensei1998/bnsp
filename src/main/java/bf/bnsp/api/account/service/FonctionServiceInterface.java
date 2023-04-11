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

    List<Fonction> updateDailyProgram(DailyProgramCreationForm dailyProgramForm, LocalDate date);

    List<Fonction> findDailyProgramDetailByCaserneAndDate(Caserne caserne, LocalDate date);

    List<Fonction> findAllDailyProgram();

    List<Fonction> findAllDailyProgramByCaserneOrderByDate(Caserne caserne);

    List<LocalDate> findDailyProgramDateByCaserne(Caserne caserne);

    List<Fonction> deleteFonctionList(Caserne caserne, LocalDate date);
}
