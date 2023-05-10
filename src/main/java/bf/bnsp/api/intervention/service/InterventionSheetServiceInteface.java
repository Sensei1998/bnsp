package bf.bnsp.api.intervention.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.dto.form.IncidentInformationForm;
import bf.bnsp.api.intervention.dto.form.IncidentInformationUpdateForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetMessageForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetConfigOutForm;
import bf.bnsp.api.intervention.model.*;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;

import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
public interface InterventionSheetServiceInteface {

    List<InterventionSheetToTeam> updateInterventionSheet(InterventionSheetConfigOutForm interventionForm, InterventionSheet interventionSheet, Agent agent);

    Optional<InterventionSheetToMessage> createInterventionMessage(InterventionSheetMessageForm messageForm, InterventionSheet interventionSheet);

    Optional<Sinister> createSinisterSheet(IncidentInformationForm incidentForm, InterventionSheet interventionSheet, Agent agent);

    Optional<Sinister> updateSinisterSheet(IncidentInformationUpdateForm incidentForm, InterventionSheet interventionSheet, Agent agent, Sinister targetedSinister);

    Optional<InterventionSheet> findActiveInterventionSheetById(InterventionFollowedKey id);

    List<InterventionSheet> findActiveInterventionSheetByCaserne(Caserne caserne);

    List<InterventionSheet> findActiveInterventionSheetByIntervention(Intervention intervention);

    List<InterventionSheetToTeam> findInterventionTeamByInterventionSheet(InterventionSheet interventionSheet);

    List<Sinister> findAllActiveSinisterByInterventionSheet(InterventionSheet interventionSheet);

    List<Sinister> findAllActiveSinisterByIntervention(Intervention intervention);

    Optional<Sinister> findActiveSinisterById(int id);

    Optional<Sinister> deleteActiveSinisterById(int id);

    Optional<InterventionSheetToMessage> findMessageById(int id);

    List<InterventionSheetToMessage> findMessagesByInterventionSheet(InterventionSheet interventionSheet);

    List<InterventionSheetToMessage> findMessagesByInterventionSheetAndTeam(InterventionSheet interventionSheet, DailyTeam equipe);

    Optional<InterventionSheetToMessage> deleteMessageById(int id);
}
