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
    public List<Fonction> updateDailyProgram(DailyProgramCreationForm dailyProgramForm, LocalDate date) {
        //Prepare Registered Data
        Optional<Equipe> targetedTeam = Optional.of(new Equipe());
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

        //Remove all Agent who not in the new list
        List<Fonction> deletedFonction = new ArrayList<>();
        List<Integer> registeredAgents = this.fonctionRepository.findAgentIdByDateAndCaserne(date, targetedTeam.get().getCaserne());
        List<Integer> formAgents = new ArrayList<>();
        for (DailyProgramTeamForm teamForm: dailyProgramForm.getTeamProgramList()) {
            for (DailyProgramFunctionForm functionForm: teamForm.getFunctionList()) {
                formAgents.add(functionForm.getAgentId());
            }
        }
        registeredAgents.removeAll(formAgents);
        for (Integer deletedAgent: registeredAgents) {
            deletedFonction.add(this.fonctionRepository.findByKeys(new FonctionKeys(date, this.agentService.findActiveAgentById(deletedAgent).get())).get());
        }
        this.fonctionRepository.deleteAll(deletedFonction);
        this.fonctionRepository.saveAll(fonctions);

        return fonctions;
    }

    @Override
    public List<Fonction> findDailyProgramDetailByCaserneAndDate(Caserne caserne, LocalDate date) {
        return this.fonctionRepository.findFonctionIdByDateAndCaserneOrderByEquipe(date, caserne);
    }

    @Override
    public List<Fonction> findAllDailyProgram() {
        return this.fonctionRepository.findAll();
    }

    @Override
    public List<Fonction> findAllDailyProgramByCaserneOrderByDate(Caserne caserne) {
        return this.fonctionRepository.findFonctionByCaserneOrderByDate(caserne);
    }

    @Override
    public List<LocalDate> findDailyProgramDateByCaserne(Caserne caserne) {
        return new ArrayList<>(new HashSet<>(this.fonctionRepository.findDailyDateByCaserneOrderByDate(caserne)));
    }

    @Override
    public List<Fonction> deleteFonctionList(Caserne caserne, LocalDate date) {
        List<Fonction> fonctions = this.findDailyProgramDetailByCaserneAndDate(caserne, date);
        for (Fonction element: this.fonctionRepository.findFonctionIdByDateAndCaserneOrderByEquipe(date, caserne)) {
            this.fonctionRepository.delete(element);
        }
        return fonctions;
    }

}
