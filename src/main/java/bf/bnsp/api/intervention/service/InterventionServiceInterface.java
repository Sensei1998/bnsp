package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.intervention.dto.form.InterventionInitAdvancedForm;
import bf.bnsp.api.intervention.dto.form.InterventionInitForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateGeneralForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateLocationForm;
import bf.bnsp.api.intervention.model.CategoryIncident;
import bf.bnsp.api.intervention.model.IncidentType;
import bf.bnsp.api.intervention.model.Intervention;

import java.util.List;
import java.util.Optional;

public interface InterventionServiceInterface {

    Optional<Intervention> createBasicIntervention(InterventionInitForm interventionForm, Agent agent);

    Optional<Intervention> createAdvancedIntervention(InterventionInitAdvancedForm interventionForm, Agent agent);

    Optional<Intervention> updateInterventionLocation(InterventionUpdateLocationForm interventionForm, Agent agent, Intervention intervention);

    Optional<Intervention> updateInterventionGeneralInfo(InterventionUpdateGeneralForm interventionForm, Agent agent, Intervention intervention);

    Optional<Intervention> findActiveInterventionById(int id);

    List<CategoryIncident> findAllCategoryIncident();

    List<IncidentType> findAllIncidentType();

    List<IncidentType> findAllIncidentTypeByCategoryIncidentId(int categoryId);

    Optional<IncidentType> findIncidentTypeById(int incidentId);

}
