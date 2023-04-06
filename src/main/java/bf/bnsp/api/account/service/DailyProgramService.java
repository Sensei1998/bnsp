package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.DailyProgramCreationForm;
import bf.bnsp.api.account.dto.form.DailyProgramFunctionForm;
import bf.bnsp.api.account.dto.form.DailyProgramTeamForm;
import bf.bnsp.api.account.model.*;
import bf.bnsp.api.account.repository.DailyProgramRepository;
import bf.bnsp.api.account.repository.FonctionRepository;
import bf.bnsp.api.account.repository.FonctionTypeRepository;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DailyProgramService implements DailyProgramServiceInterface{

    @Autowired
    private DailyProgramRepository dailyProgramRepository;

    @Autowired
    private FonctionTypeRepository fonctionTypeRepository;

    @Autowired
    private FonctionRepository fonctionRepository;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private AgentService agentService;

    @Override
    public Optional<DailyProgram> createDailyProgram(DailyProgramCreationForm dailyProgramForm, Caserne caserne) {
        Optional<Equipe> targetedTeam;
        Optional<Agent> targetedAgent;
        Optional<FonctionType> targetedFunction;
        List<Fonction> fonctionList = new ArrayList<>();
        for (DailyProgramTeamForm teams: dailyProgramForm.getTeamProgramList()) {
            targetedTeam = this.equipeService.findActiveEquipeById(teams.getEquipeId());
            if(targetedTeam.isEmpty()) return Optional.empty();
            else {
                for (DailyProgramFunctionForm fonction: teams.getFunctionList()) {
                    targetedAgent = this.agentService.findActiveAgentById(fonction.getAgentId());
                    targetedFunction = this.fonctionTypeRepository.findById(fonction.getFunctionTypeId());
                    if(targetedAgent.isEmpty() || targetedFunction.isEmpty()) return Optional.empty();
                    else fonctionList.add(new Fonction(targetedTeam.get(), targetedFunction.get(), targetedAgent.get()));
                }
            }
        }
        DailyProgramKeys keys = new DailyProgramKeys(dailyProgramForm.getDate(), caserne);
        DailyProgram dailyProgram = new DailyProgram(keys, fonctionList);
        this.dailyProgramRepository.save(dailyProgram);
        return Optional.of(dailyProgram);
    }

    @Override
    public Optional<DailyProgram> updateDailyProgram(DailyProgramCreationForm dailyProgramForm, Caserne caserne, DailyProgram targetedProgram) {
        Optional<Equipe> targetedTeam;
        Optional<Agent> targetedAgent;
        Optional<Fonction> targetedFonction;
        Optional<FonctionType> targetedFunction;
        List<Fonction> fonctionList = new ArrayList<>();
        for (DailyProgramTeamForm teams: dailyProgramForm.getTeamProgramList()) {
            targetedTeam = this.equipeService.findActiveEquipeById(teams.getEquipeId());
            if(targetedTeam.isEmpty()) return Optional.empty();
            else {
                for (DailyProgramFunctionForm fonction: teams.getFunctionList()) {
                    if(fonction.getId().isPresent()){
                        targetedFonction = this.fonctionRepository.findById(fonction.getId().get());
                    }
                    targetedAgent = this.agentService.findActiveAgentById(fonction.getAgentId());
                    targetedFunction = this.fonctionTypeRepository.findById(fonction.getFunctionTypeId());
                    if(targetedAgent.isEmpty() || targetedFunction.isEmpty()) return Optional.empty();
                    else fonctionList.add(new Fonction(targetedTeam.get(), targetedFunction.get(), targetedAgent.get()));
                }
            }
        }
        targetedProgram.setFonctions(fonctionList);
        this.dailyProgramRepository.save(targetedProgram);
        return Optional.of(targetedProgram);
    }

    @Override
    public Optional<DailyProgram> findDailyProgramByCaserneAndDate(Caserne caserne, LocalDate date) {
        return this.dailyProgramRepository.findById(new DailyProgramKeys(date, caserne));
    }

    @Override
    public List<DailyProgram> findAllDailyProgram() {
        return this.dailyProgramRepository.findAll();
    }

    @Override
    public List<DailyProgram> findAllDailyProgramByCaserne(Caserne caserne) {
        return this.dailyProgramRepository.findDailyProgramByCaserne(caserne);
    }

    @Override
    public Optional<DailyProgram> deleteDailyProgramByCaserneAndDate(Caserne caserne, Date date) {
        return Optional.empty();
    }
}
