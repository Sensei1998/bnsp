package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.service.DailyProgramService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.dto.form.IncidentInformationForm;
import bf.bnsp.api.intervention.dto.form.IncidentInformationUpdateForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetMessageForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetConfigOutForm;
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
        for(InterventionSheetOut element: interventionForm.getInterventionSheet()){
            targetedEquipe = this.dailyProgramService.findActiveDailyTeamById(element.getEquipeId());
            if(targetedEquipe.isEmpty()) return new ArrayList<>();
            else{
                tmpTeamConfig = new InterventionSheetToTeam(interventionSheet, targetedEquipe.get());
                if(element.getDeparture().isPresent()) tmpTeamConfig.setDeparture(element.getDeparture().get());
                if(element.getPresentation().isPresent()) tmpTeamConfig.setDeparture(element.getPresentation().get());
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
