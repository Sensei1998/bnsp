package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.intervention.dto.form.InterventionInitAdvancedForm;
import bf.bnsp.api.intervention.dto.form.InterventionInitForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateGeneralForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateLocationForm;
import bf.bnsp.api.intervention.model.Intervention;

import java.util.Optional;

public interface InterventionServiceInterface {

    Optional<Intervention> createBasicIntervention(InterventionInitForm interventionForm, Agent agent);

    Optional<Intervention> createAdvancedIntervention(InterventionInitAdvancedForm interventionForm, Agent agent);

    Optional<Intervention> updateInterventionLocation(InterventionUpdateLocationForm interventionForm, Agent agent, Intervention intervention);

    Optional<Intervention> updateInterventionGeneralInfo(InterventionUpdateGeneralForm interventionForm, Agent agent, Intervention intervention);

    Optional<Intervention> findActiveInterventionById(int id);

}
