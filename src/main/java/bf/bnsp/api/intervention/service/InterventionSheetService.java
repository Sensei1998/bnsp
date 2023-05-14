package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.service.DailyProgramService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.dto.form.*;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionSheetOut;
import bf.bnsp.api.intervention.dto.form.partialData.PersonInfo;
import bf.bnsp.api.intervention.model.*;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import bf.bnsp.api.intervention.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Service
public class InterventionSheetService implements InterventionSheetServiceInteface{

    @Autowired
    private InterventionSheetRepository interventionSheetRepository;

    @Autowired
    private InterventionSheetToTeamRepository interventionSheetToTeamRepository;

    @Autowired
    private InterventionSheetToMessageRepository interventionSheetToMessageRepository;

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private SinisterRepository sinisterRepository;

    @Autowired
    private DailyProgramService dailyProgramService;

    @Override
    public List<InterventionSheetToTeam> updateInterventionSheet(InterventionSheetConfigOutForm interventionForm, InterventionSheet interventionSheet, Agent agent) {
        List<InterventionSheetToTeam> teamConfigList = new ArrayList<>();
        InterventionSheetToTeam tmpTeamConfig;
        Optional<DailyTeam> targetedEquipe;
        Optional<InterventionSheetToTeam> targetedElement;

        List<Long> registeredTeam = this.interventionSheetToTeamRepository.findTeamIdByInterventionSheet(interventionSheet);
        List<Long> savedTeam;
        List<Long> tmpAddedTeam = new ArrayList<>();
        List<InterventionSheetToTeam> deletedTeam = new ArrayList<>();

        for(InterventionSheetOut element : interventionForm.getInterventionSheet()) tmpAddedTeam.add((long) element.getEquipeId());

        savedTeam = new ArrayList<>(tmpAddedTeam);
        registeredTeam.removeAll(tmpAddedTeam);
        savedTeam.removeAll(registeredTeam);
        for(long deletedElement : registeredTeam){
            targetedEquipe = this.dailyProgramService.findActiveDailyTeamById(deletedElement);
            if(targetedEquipe.isEmpty()) return new ArrayList<>();
            else {
                targetedElement = this.interventionSheetToTeamRepository.findByInterventionSheetAndEquipe(interventionSheet, targetedEquipe.get());
                deletedTeam.add(targetedElement.get());
            }
        }

        for (InterventionSheetToTeam element: deletedTeam){
            if(element.isActive()){
                element.setHidden(true);
                this.interventionSheetToTeamRepository.save(element);
            }
            else this.interventionSheetToTeamRepository.delete(element);
        }

        for(InterventionSheetOut element : interventionForm.getInterventionSheet()){
            if(savedTeam.contains((long) element.getEquipeId())){
                targetedEquipe = this.dailyProgramService.findActiveDailyTeamById(element.getEquipeId());
                if(targetedEquipe.isEmpty()) return new ArrayList<>();
                targetedElement = this.interventionSheetToTeamRepository.findByInterventionSheetAndEquipe(interventionSheet, targetedEquipe.get());
                tmpTeamConfig = targetedElement.isEmpty() ? new InterventionSheetToTeam(interventionSheet, targetedEquipe.get()) : targetedElement.get();
                if(element.getDeparture().isPresent()) tmpTeamConfig.setDeparture(element.getDeparture().get());
                if(element.getPresentation().isPresent()) tmpTeamConfig.setPresentation(element.getPresentation().get());
                if(element.getAvailable().isPresent()){
                    tmpTeamConfig.setAvailable(element.getAvailable().get());
                    tmpTeamConfig.setActive(false);
                }
                if(element.getCheckIn().isPresent()){
                    tmpTeamConfig.setCheckIn(element.getCheckIn().get());
                    tmpTeamConfig.setActive(false);
                }
                if(tmpTeamConfig.isHidden()) tmpTeamConfig.setHidden(false);
                teamConfigList.add(tmpTeamConfig);
            }
        }
        interventionSheet.setActive(true);
        interventionSheet.setAgentBCOT(agent);
        this.interventionSheetRepository.save(interventionSheet);
        this.interventionSheetToTeamRepository.saveAll(teamConfigList);
        Intervention intervention = interventionSheet.getKey().getIntervention();
        intervention.setStatus("En cours");
        this.interventionRepository.save(intervention);
        return teamConfigList;
    }

    @Override
    public Optional<InterventionSheetToMessage> createInterventionMessage(InterventionSheetMessageForm messageForm, InterventionSheet interventionSheet) {
        Optional<DailyTeam> targetedEquipe;
        targetedEquipe = this.dailyProgramService.findActiveDailyTeamById(messageForm.getEquipeId());
        if(targetedEquipe.isEmpty()) return Optional.empty();
        else {
            InterventionSheetToMessage message = new InterventionSheetToMessage(interventionSheet, targetedEquipe.get(), messageForm.getMessage());
            return Optional.of(this.interventionSheetToMessageRepository.save(message));
        }
    }

    @Override
    public Optional<Sinister> createSinisterSheet(IncidentInformationForm incidentForm, InterventionSheet interventionSheet, Agent agent) {
        List<Person> owner = new ArrayList<>();
        List<Person> victims = new ArrayList<>();
        Person tmpPerson;
        for(PersonInfo element: incidentForm.getSinistres()){
            tmpPerson = new Person(element.getFirstname(), element.getLastname(), agent);
            if(element.getAddress().isPresent()) tmpPerson.setAddress(element.getAddress().get());
            victims.add(tmpPerson);
        }
        for(PersonInfo element: incidentForm.getProprietaires()){
            tmpPerson = new Person(element.getFirstname(), element.getLastname(), agent);
            if(element.getAddress().isPresent()) tmpPerson.setAddress(element.getAddress().get());
            owner.add(tmpPerson);
        }
        Sinister sinister = new Sinister(interventionSheet, incidentForm.getDegats(), incidentForm.getCommentaires());
        sinister.setOwners(owner);
        sinister.setVictims(victims);
        this.sinisterRepository.save(sinister);
        return Optional.of(sinister);
    }

    @Override
    public Optional<Sinister> updateSinisterSheet(IncidentInformationUpdateForm incidentForm, InterventionSheet interventionSheet, Agent agent, Sinister targetedSinister) {
        List<Person> owner = new ArrayList<>();
        List<Person> victims = new ArrayList<>();
        Person tmpPerson;
        for(PersonInfo element: incidentForm.getSinistres()){
            tmpPerson = new Person(element.getFirstname(), element.getLastname(), agent);
            if(element.getAddress().isPresent()) tmpPerson.setAddress(element.getAddress().get());
            victims.add(tmpPerson);
        }
        for(PersonInfo element: incidentForm.getProprietaires()){
            tmpPerson = new Person(element.getFirstname(), element.getLastname(), agent);
            if(element.getAddress().isPresent()) tmpPerson.setAddress(element.getAddress().get());
            owner.add(tmpPerson);
        }
        targetedSinister.setDamages(incidentForm.getDegats());
        targetedSinister.setComments(incidentForm.getCommentaires());
        targetedSinister.setUpdateBy(agent);
        targetedSinister.setOwners(owner);
        targetedSinister.setVictims(victims);
        this.sinisterRepository.save(targetedSinister);
        return Optional.of(targetedSinister);
    }

    @Override
    public Optional<InterventionSheet> findActiveInterventionSheetById(InterventionFollowedKey id) {
        return this.interventionSheetRepository.findById(id);
    }

    @Override
    public List<InterventionSheet> findActiveInterventionSheetByCaserne(Caserne caserne) {
        return this.interventionSheetRepository.findInterventionSheetByCaserne(caserne);
    }

    @Override
    public List<InterventionSheet> findActiveInterventionSheetByIntervention(Intervention intervention) {
        return this.interventionSheetRepository.findInterventionSheetByIntervention(intervention);
    }

    @Override
    public List<InterventionSheetToTeam> findInterventionTeamByInterventionSheet(InterventionSheet interventionSheet) {
        return this.interventionSheetToTeamRepository.findByInterventionSheetAndHiddenFalse(interventionSheet);
    }

    @Override
    public Optional<InterventionSheetToTeam> updateInterventionTeam(InterventionTeamUpdate teamForm, InterventionSheetToTeam interventionTeam) {
        if(teamForm.getDeparture().isPresent()) interventionTeam.setDeparture(teamForm.getDeparture().get());
        if(teamForm.getPresentation().isPresent()) interventionTeam.setPresentation(teamForm.getPresentation().get());
        if(teamForm.getAvailable().isPresent()){
            interventionTeam.setAvailable(teamForm.getAvailable().get());
            interventionTeam.setActive(false);
        }
        if(teamForm.getCheckIn().isPresent()){
            interventionTeam.setCheckIn(teamForm.getCheckIn().get());
            interventionTeam.setActive(false);
        }
        this.interventionSheetToTeamRepository.save(interventionTeam);
        return Optional.empty();
    }

    @Override
    public Optional<InterventionSheetToTeam> findInterventionTeamById(int id) {
        return this.interventionSheetToTeamRepository.findByIdAndHiddenFalse(id);
    }

    @Override
    public Optional<InterventionSheetToTeam> deleteInterventionTeam(InterventionSheetToTeam interventionSheetToTeam) {
        if(interventionSheetToTeam.isActive()) this.interventionSheetToTeamRepository.delete(interventionSheetToTeam);
        else{
            interventionSheetToTeam.setHidden(true);
            this.interventionSheetToTeamRepository.save(interventionSheetToTeam);
        }
        return Optional.of(interventionSheetToTeam);
    }

    @Override
    public List<Sinister> findAllActiveSinisterByInterventionSheet(InterventionSheet interventionSheet) {
        return this.sinisterRepository.findByInterventionSheet(interventionSheet);
    }

    @Override
    public List<Sinister> findAllActiveSinisterByIntervention(Intervention intervention) {
        return this.sinisterRepository.findSinisterByIntervention(intervention);
    }

    @Override
    public Optional<Sinister> findActiveSinisterById(int id) {
        return this.sinisterRepository.findById(id);
    }

    @Override
    public Optional<Sinister> deleteActiveSinisterById(int id) {
        Optional<Sinister> response = this.findActiveSinisterById(id);
        if(response.isPresent()) this.sinisterRepository.delete(response.get());
        return response;
    }

    @Override
    public Optional<InterventionSheetToMessage> findMessageById(int id) {
        return this.interventionSheetToMessageRepository.findById(id);
    }

    @Override
    public List<InterventionSheetToMessage> findMessagesByInterventionSheet(InterventionSheet interventionSheet) {
        return this.interventionSheetToMessageRepository.findByInterventionSheet(interventionSheet);
    }

    @Override
    public List<InterventionSheetToMessage> findMessagesByInterventionSheetAndTeam(InterventionSheet interventionSheet, DailyTeam equipe) {
        return this.interventionSheetToMessageRepository.findByInterventionSheetAndEquipe(interventionSheet, equipe);
    }

    @Override
    public Optional<InterventionSheetToMessage> deleteMessageById(int id) {
        Optional<InterventionSheetToMessage> response = this.findMessageById(id);
        if(response.isPresent()) this.interventionSheetToMessageRepository.delete(response.get());
        return response;
    }


}
