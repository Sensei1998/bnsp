package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.DailyProgramCreationForm;
import bf.bnsp.api.account.dto.form.DailyProgramFunctionForm;
import bf.bnsp.api.account.dto.form.DailyProgramTeamForm;
import bf.bnsp.api.account.model.*;
import bf.bnsp.api.account.repository.FonctionRepository;
import bf.bnsp.api.account.repository.FonctionTypeRepository;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class FonctionService implements FonctionServiceInterface {


    @Autowired
    private FonctionTypeRepository fonctionTypeRepository;

    @Autowired
    private FonctionRepository fonctionRepository;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private AgentService agentService;

    @Override
    public List<Fonction> createDailyProgram(DailyProgramCreationForm dailyProgramForm) {
        Optional<Equipe> targetedTeam;
        Optional<Agent> targetedAgent;
        Optional<FonctionType> targetedFunction;

        List<Fonction> fonctions = new ArrayList<>();

        for (DailyProgramTeamForm teamsProgram: dailyProgramForm.getTeamProgramList()) {
            targetedTeam = this.equipeService.findActiveEquipeById(teamsProgram.getEquipeId());
            if(targetedTeam.isEmpty()) return new ArrayList<>();
            else{
                for (DailyProgramFunctionForm fonction: teamsProgram.getFunctionList()) {
                    targetedAgent = this.agentService.findActiveAgentById(fonction.getAgentId());
                    targetedFunction = this.fonctionTypeRepository.findById(fonction.getFunctionTypeId());
                    if(targetedAgent.isEmpty() || targetedFunction.isEmpty()) return new ArrayList<>();
                    else fonctions.add(new Fonction(new FonctionKeys(dailyProgramForm.getDate(), targetedAgent.get()), targetedFunction.get(), targetedTeam.get()));
                }
            }
        }
        this.fonctionRepository.saveAll(fonctions);
        return fonctions;
    }

    @Override
    public List<Fonction> updateDailyProgram(DailyProgramCreationForm dailyProgramForm, LocalDate date, Caserne caserne) {
        return null;
    }

    @Override
    public List<Fonction> findDailyProgramDetailByCaserneAndDate(Caserne caserne, LocalDate date) {
        return null;
    }

    @Override
    public List<Fonction> findDailyProgramByCaserneAndDate(Caserne caserne, LocalDate date) {
        return null;
    }

    @Override
    public List<Fonction> findAllDailyProgram() {
        return null;
    }

    @Override
    public List<Fonction> findAllDailyProgramByCaserne(Caserne caserne) {
        return null;
    }

    @Override
    public List<Fonction> deleteDailyProgramByCaserneAndDate(Caserne caserne, Date date) {
        return null;
    }


}
