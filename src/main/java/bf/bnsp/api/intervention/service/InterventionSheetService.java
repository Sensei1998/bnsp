package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.service.EquipeService;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.intervention.dto.form.InterventionSheetMessageForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetConfigOutForm;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionSheetOut;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.InterventionSheetToMessage;
import bf.bnsp.api.intervention.model.InterventionSheetToTeam;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import bf.bnsp.api.intervention.repository.InterventionSheetRepository;
import bf.bnsp.api.intervention.repository.InterventionSheetToMessageRepository;
import bf.bnsp.api.intervention.repository.InterventionSheetToTeamRepository;
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
    private EquipeService equipeService;

    @Override
    public List<InterventionSheetToTeam> updateInterventionSheet(InterventionSheetConfigOutForm interventionForm, InterventionSheet interventionSheet) {
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
    public Optional<InterventionSheet> findActiveInterventionSheetById(InterventionFollowedKey id) {
        return this.interventionSheetRepository.findById(id);
    }


}
