package bf.bnsp.api.intervention.service;

import bf.bnsp.api.intervention.dto.form.InterventionSheetMessageForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetConfigOutForm;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.InterventionSheetToMessage;
import bf.bnsp.api.intervention.model.InterventionSheetToTeam;

import java.util.List;
import java.util.Optional;

public interface InterventionSheetServiceInteface {

    List<InterventionSheetToTeam> updateInterventionSheet(InterventionSheetConfigOutForm interventionForm, InterventionSheet interventionSheet);

    Optional<InterventionSheetToMessage> createInterventionMessage(InterventionSheetMessageForm messageForm, InterventionSheet interventionSheet);
}
