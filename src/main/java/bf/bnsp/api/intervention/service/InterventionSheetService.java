package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.service.EquipeService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.intervention.dto.form.IncidentInformationForm;
import bf.bnsp.api.intervention.dto.form.IncidentInformationUpdateForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetMessageForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetConfigOutForm;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionSheetOut;
import bf.bnsp.api.intervention.dto.form.partialData.PersonInfo;
import bf.bnsp.api.intervention.model.*;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import bf.bnsp.api.intervention.repository.InterventionSheetRepository;
import bf.bnsp.api.intervention.repository.InterventionSheetToMessageRepository;
import bf.bnsp.api.intervention.repository.InterventionSheetToTeamRepository;
import bf.bnsp.api.intervention.repository.SinisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InterventionSheetService implements InterventionSheetServiceInteface{

    @Autowired
    private InterventionSheetRepository interventionSheetRepository;

    @Autowired
    private InterventionSheetToTeamRepository interventionSheetToTeamRepository;

    @Autowired
    private InterventionSheetToMessageRepository interventionSheetToMessageRepository;

    @Autowired
    private SinisterRepository sinisterRepository;

    @Autowired
    private EquipeService equipeService;

    @Override
    public List<InterventionSheetToTeam> updateInterventionSheet(InterventionSheetConfigOutForm interventionForm, InterventionSheet interventionSheet, Agent agent) {
        List<InterventionSheetToTeam> teamConfigList = new ArrayList<>();
        InterventionSheetToTeam tmpTeamConfig;
        Optional<Equipe> targetedEquipe;
        for(InterventionSheetOut element: interventionForm.getInterventionSheet()){
            targetedEquipe = this.equipeService.findActiveEquipeById(element.getEquipeId());
            if(targetedEquipe.isEmpty()) return new ArrayList<>();
            else{
                tmpTeamConfig = new InterventionSheetToTeam(interventionSheet, targetedEquipe.get());
                if(targetedEquipe.get().getEngin() != null) tmpTeamConfig.setEngin(targetedEquipe.get().getEngin());
                if(element.getDeparture().isPresent()) tmpTeamConfig.setDeparture(element.getDeparture().get());
                if(element.getPresentation().isPresent()) tmpTeamConfig.setDeparture(element.getPresentation().get());
                teamConfigList.add(tmpTeamConfig);
            }
        }
        interventionSheet.setActive(true);
        interventionSheet.setAgentBCOT(agent);
        this.interventionSheetRepository.save(interventionSheet);
        this.interventionSheetToTeamRepository.saveAll(teamConfigList);
        return teamConfigList;
    }

    @Override
    public Optional<InterventionSheetToMessage> createInterventionMessage(InterventionSheetMessageForm messageForm, InterventionSheet interventionSheet) {
        Optional<Equipe> targetedEquipe;
        targetedEquipe = this.equipeService.findActiveEquipeById(messageForm.getEquipeId());
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
    public List<InterventionSheetToMessage> findMessagesByInterventionSheetAndTeam(InterventionSheet interventionSheet, Equipe equipe) {
        return this.interventionSheetToMessageRepository.findByInterventionSheetAndEquipe(interventionSheet, equipe);
    }

    @Override
    public Optional<InterventionSheetToMessage> deleteMessageById(int id) {
        Optional<InterventionSheetToMessage> response = this.findMessageById(id);
        if(response.isPresent()) this.interventionSheetToMessageRepository.delete(response.get());
        return response;
    }


}
