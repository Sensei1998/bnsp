package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.DailyProgramInitForm;
import bf.bnsp.api.account.dto.form.DailyTeamAddForm;
import bf.bnsp.api.account.dto.form.DailyTeamUpdateForm;
import bf.bnsp.api.account.dto.form.partialData.DailyTeamForm;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.model.FonctionType;
import bf.bnsp.api.caserne.model.Caserne;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyProgramServiceInterface {

    Optional<DailyProgram> createDailyProgram(DailyProgramInitForm programForm, Caserne caserne);

    Optional<DailyProgram> addTeamToDailyProgram(DailyTeamAddForm teamForm, DailyProgram dailyProgram);

    Optional<DailyTeam> updateTeamComposition(DailyTeamUpdateForm teamForm, DailyTeam dailyTeam);

    Optional<DailyTeam> findActiveDailyTeamById(long id);

    Optional<DailyTeam> deleteDailyTeam(DailyTeam dailyTeam);

    Optional<DailyProgram> findActiveDailyProgramById(long id);

    Optional<DailyProgram> findActiveDailyProgramByDateAndCaserne(LocalDate date, Caserne caserne);

    Optional<FonctionType> findCurrentFonctionByAgent(Agent agent);
}
