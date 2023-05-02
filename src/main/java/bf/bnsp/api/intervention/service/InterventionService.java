package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.intervention.dto.form.InterventionInitAdvancedForm;
import bf.bnsp.api.intervention.dto.form.InterventionInitForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateGeneralForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateLocationForm;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionCaserne;
import bf.bnsp.api.intervention.model.CategoryIncident;
import bf.bnsp.api.intervention.model.IncidentType;
import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.additional.CallerInfo;
import bf.bnsp.api.intervention.model.additional.Incident;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import bf.bnsp.api.intervention.model.additional.Localisation;
import bf.bnsp.api.intervention.repository.CategoryIncidentRepository;
import bf.bnsp.api.intervention.repository.IncidentTypeRepository;
import bf.bnsp.api.intervention.repository.InterventionRepository;
import bf.bnsp.api.intervention.repository.InterventionSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Berickal
 */
@Service
public class InterventionService implements InterventionServiceInterface{
    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private InterventionSheetRepository interventionSheetRepository;

    @Autowired
    private CategoryIncidentRepository categoryIncidentRepository;

    @Autowired
    private IncidentTypeRepository incidentTypeRepository;

    @Autowired
    private CaserneService caserneService;

    @Override
    public Optional<Intervention> createBasicIntervention(InterventionInitForm interventionForm, Agent agent) {
        CallerInfo callerInfo = new CallerInfo(interventionForm.getProvenance(), interventionForm.getPhoneNumber(), interventionForm.getName(), interventionForm.getAddress(), new Localisation(interventionForm.getLongitude(), interventionForm.getLatitude(), interventionForm.getPrecision()));
        Intervention intervention = new Intervention(agent, callerInfo);
        intervention.setDate(LocalDateTime.of(interventionForm.getDate(), interventionForm.getTime()));
        if(interventionForm.getIncident().isPresent()){
            Optional<IncidentType> incidentType = this.findIncidentTypeById(interventionForm.getIncident().get().getIncidentTypeId());
            Optional<CategoryIncident> categoryIncident = this.incidentTypeRepository.findCategoryIncidentByIncidentType(interventionForm.getIncident().get().getIncidentTypeId());
            if(incidentType.isEmpty()) return Optional.empty();
            Incident incident = new Incident(categoryIncident.get().getCategory(), incidentType.get().getDesignation(), interventionForm.getIncident().get().getComments());
            intervention.setIncident(incident);
        }
        this.interventionRepository.save(intervention);
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> createAdvancedIntervention(InterventionInitAdvancedForm interventionForm, Agent agent) {
        List<Caserne> casernes = new ArrayList<>();
        Optional<Caserne> targetedCaserne;
        Optional<CategoryIncident> categoryIncident = this.categoryIncidentRepository.findById(interventionForm.getIncident().getCategoryId().get());
        Optional<IncidentType> incidentType = this.findIncidentTypeById(interventionForm.getIncident().getIncidentTypeId());
        if(categoryIncident.isEmpty() || incidentType.isEmpty()) return Optional.empty();

        CallerInfo callerInfo = new CallerInfo(interventionForm.getAppelant().getProvenance(), interventionForm.getAppelant().getPhoneNumber(), interventionForm.getAppelant().getName(), interventionForm.getAppelant().getAddress(), new Localisation(interventionForm.getAppelant().getLongitude(), interventionForm.getAppelant().getLatitude(), interventionForm.getAppelant().getPrecision()));
        Intervention intervention = new Intervention(agent, callerInfo);
        Incident incident = new Incident(categoryIncident.get().getCategory(), incidentType.get().getDesignation(), interventionForm.getIncident().getComments());
        intervention.setIncident(incident);
        intervention.setDate(LocalDateTime.of(interventionForm.getDate(), interventionForm.getTime()));
        for (InterventionCaserne element: interventionForm.getCasernes()) {
            targetedCaserne = caserneService.findActiveCaserneById(element.getCaserneId());
            if(targetedCaserne.isPresent()) casernes.add(targetedCaserne.get());
            else return Optional.empty();
        }
        this.interventionRepository.save(intervention);
        int index = 0;
        InterventionSheet interventionSheet;
        for (Caserne element: casernes) {
            interventionSheet = new InterventionSheet(new InterventionFollowedKey(intervention, element));
            if(interventionForm.getCasernes().get(index).getMessage().isPresent()) interventionSheet.setMessage(interventionForm.getCasernes().get(index).getMessage().get());
            this.interventionSheetRepository.save(interventionSheet);
        }
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> updateInterventionLocation(InterventionUpdateLocationForm interventionForm, Agent agent, Intervention intervention) {
        CallerInfo callerInfo = new CallerInfo(interventionForm.getProvenance(), interventionForm.getPhoneNumber(), interventionForm.getName(), interventionForm.getAddress(), new Localisation(interventionForm.getLongitude(), interventionForm.getLatitude(), interventionForm.getPrecision()));
        intervention.setCaller(callerInfo);

        Optional<CategoryIncident> categoryIncident = this.categoryIncidentRepository.findById(interventionForm.getIncident().get().getCategoryId().get());
        Optional<IncidentType> incidentType = this.findIncidentTypeById(interventionForm.getIncident().get().getIncidentTypeId());
        if(categoryIncident.isEmpty() || incidentType.isEmpty()) return Optional.empty();
        intervention.setIncident(new Incident(categoryIncident.get().getCategory(), incidentType.get().getDesignation(), interventionForm.getIncident().get().getComments()));
        this.interventionRepository.save(intervention);
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> updateInterventionGeneralInfo(InterventionUpdateGeneralForm interventionForm, Agent agent, Intervention intervention) {

        HashMap<Integer, Optional<String>> interventionCaserne = new HashMap<>();
        List<Integer> registeredCaserne = this.interventionSheetRepository.findCaserneIdByIntervention(intervention);
        List<InterventionCaserne> interventionCasernes = interventionForm.getCasernes();
        List<Integer> addedCaserne = new ArrayList<>();
        List<Integer> tmpAddedCaserne = new ArrayList<>();
        List<InterventionSheet> deletedInterventionSheet = new ArrayList<>();
        List<Caserne> casernes = new ArrayList<>();
        Optional<Caserne> targetedCaserne;

        for(InterventionCaserne element: interventionCasernes){
            tmpAddedCaserne.add(element.getCaserneId());
            interventionCaserne.put(element.getCaserneId(), element.getMessage());
        }
        addedCaserne = new ArrayList<>(tmpAddedCaserne);
        addedCaserne.removeAll(registeredCaserne);
        registeredCaserne.removeAll(tmpAddedCaserne);
        for(int deletedCaserne : registeredCaserne){
            deletedInterventionSheet.add(this.interventionSheetRepository.findById(new InterventionFollowedKey(intervention, this.caserneService.findActiveCaserneById(deletedCaserne).get())).get());
        }
        for (Integer element: addedCaserne) {
            targetedCaserne = caserneService.findActiveCaserneById(element);
            if(targetedCaserne.isPresent()) casernes.add(targetedCaserne.get());
            else return Optional.empty();
        }

        for (InterventionSheet element: deletedInterventionSheet) {
            if(element.isActive()){
                element.setHidden(true);
                this.interventionSheetRepository.save(element);
            }
            else this.interventionSheetRepository.delete(element);
        }
        InterventionSheet interventionSheet;
        for (Caserne element: casernes) {
            interventionSheet = new InterventionSheet(new InterventionFollowedKey(intervention, element));
            if(interventionCaserne.get(element.getId()).isPresent()) interventionSheet.setMessage(interventionCaserne.get(element.getId()).get());
            this.interventionSheetRepository.save(interventionSheet);
        }
        return Optional.of(intervention);
    }

    @Override
    public Optional<Intervention> findActiveInterventionById(int id) {
        return this.interventionRepository.findById(id);
    }

    @Override
    public List<Intervention> findAllActiveIntervention() {
        return this.interventionRepository.findAll();
    }

    @Override
    public List<CategoryIncident> findAllCategoryIncident() {
        return this.categoryIncidentRepository.findAll();
    }

    @Override
    public List<IncidentType> findAllIncidentType() {
        return this.incidentTypeRepository.findAll();
    }

    @Override
    public List<IncidentType> findAllIncidentTypeByCategoryIncidentId(int categoryId) {
        Optional<CategoryIncident> categoryIncident = this.categoryIncidentRepository.findById(categoryId);
        return categoryIncident.isPresent() ? categoryIncident.get().getTypes() : new ArrayList<>();
    }

    @Override
    public Optional<IncidentType> findIncidentTypeById(int incidentId) {
        return this.incidentTypeRepository.findById(incidentId);
    }


}
