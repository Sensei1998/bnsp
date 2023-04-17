package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.intervention.dto.form.InterventionInitAdvancedForm;
import bf.bnsp.api.intervention.dto.form.InterventionInitForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateGeneralForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateLocationForm;
import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.additional.CallerInfo;
import bf.bnsp.api.intervention.model.additional.Incident;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import bf.bnsp.api.intervention.model.additional.Localisation;
import bf.bnsp.api.intervention.repository.InterventionRepository;
import bf.bnsp.api.intervention.repository.InterventionSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class InterventionService implements InterventionServiceInterface{
    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private InterventionSheetRepository interventionSheetRepository;

    @Autowired
    private CaserneService caserneService;

    @Override
    public Optional<Intervention> createBasicIntervention(InterventionInitForm interventionForm, Agent agent) {
        CallerInfo callerInfo = new CallerInfo(interventionForm.getProvenance(), interventionForm.getPhoneNumber(), interventionForm.getName(), interventionForm.getAddress(), new Localisation(interventionForm.getLongitude(), interventionForm.getLatitude(), interventionForm.getPrecision()));
        Intervention intervention = new Intervention(agent, callerInfo);
        this.interventionRepository.save(intervention);
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> createAdvancedIntervention(InterventionInitAdvancedForm interventionForm, Agent agent) {
        List<Caserne> casernes = new ArrayList<>();
        Optional<Caserne> targetedCaserne;
        CallerInfo callerInfo = new CallerInfo(interventionForm.getAppelant().getProvenance(), interventionForm.getAppelant().getPhoneNumber(), interventionForm.getAppelant().getName(), interventionForm.getAppelant().getAddress(), new Localisation(interventionForm.getAppelant().getLongitude(), interventionForm.getAppelant().getLatitude(), interventionForm.getAppelant().getPrecision()));
        Intervention intervention = new Intervention(agent, callerInfo);
        Incident incident = new Incident(interventionForm.getIncident().getCategory(), interventionForm.getIncident().getLibelle(), interventionForm.getIncident().getComments());
        intervention.setIncident(incident);
        for (Integer element: interventionForm.getCasernes()) {
            targetedCaserne = caserneService.findActiveCaserneById(element);
            if(targetedCaserne.isPresent()) casernes.add(targetedCaserne.get());
            else return Optional.empty();
        }
        this.interventionRepository.save(intervention);
        for (Caserne element: casernes) {
            this.interventionSheetRepository.save(new InterventionSheet(new InterventionFollowedKey(intervention, element)));
        }
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> updateInterventionLocation(InterventionUpdateLocationForm interventionForm, Agent agent, Intervention intervention) {
        CallerInfo callerInfo = new CallerInfo(interventionForm.getProvenance(), interventionForm.getPhoneNumber(), interventionForm.getName(), interventionForm.getAddress(), new Localisation(interventionForm.getLongitude(), interventionForm.getLatitude(), interventionForm.getPrecision()));
        intervention.setCaller(callerInfo);
        this.interventionRepository.save(intervention);
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> updateInterventionGeneralInfo(InterventionUpdateGeneralForm interventionForm, Agent agent, Intervention intervention) {
        Incident incident = new Incident(interventionForm.getCategory(), interventionForm.getLibelle(), interventionForm.getComments());
        intervention.setIncident(incident);
        this.interventionRepository.save(intervention);


        List<Integer> registeredCaserne = this.interventionSheetRepository.findCaserneIdByIntervention(intervention);
        HashSet<Integer> addedCaserne = interventionForm.getCaserneId();
        List<InterventionSheet> deletedInterventionSheet = new ArrayList<>();
        List<Caserne> casernes = new ArrayList<>();
        Optional<Caserne> targetedCaserne;
        addedCaserne.removeAll(registeredCaserne);
        registeredCaserne.removeAll(interventionForm.getCaserneId());
        for(int deletedCaserne : registeredCaserne){
            deletedInterventionSheet.add(this.interventionSheetRepository.findById(new InterventionFollowedKey(intervention, this.caserneService.findActiveCaserneById(deletedCaserne).get())).get());
        }
        for (Integer element: addedCaserne) {
            targetedCaserne = caserneService.findActiveCaserneById(element);
            if(targetedCaserne.isPresent()) casernes.add(targetedCaserne.get());
            else return Optional.empty();
        }
        //Replace by a function that going to check if the targeted interventionSheet is active before remove it
        this.interventionSheetRepository.deleteAll(deletedInterventionSheet);
        for (Caserne element: casernes) {
            this.interventionSheetRepository.save(new InterventionSheet(new InterventionFollowedKey(intervention, element)));
        }
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> findActiveInterventionById(int id) {
        return this.interventionRepository.findById(id);
    }


}
