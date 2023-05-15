package bf.bnsp.api.tools.mappingTools;

import bf.bnsp.api.account.model.DailyTeamMember;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.dto.response.IncidentResponseMultiCaserne;
import bf.bnsp.api.intervention.dto.response.IncidentResponseOneCaserne;
import bf.bnsp.api.intervention.dto.response.InterventionResponse;
import bf.bnsp.api.intervention.dto.response.MessageResponse;
import bf.bnsp.api.intervention.dto.response.partialData.*;
import bf.bnsp.api.intervention.model.*;
import bf.bnsp.api.intervention.service.InterventionService;
import bf.bnsp.api.intervention.service.InterventionSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Service
public class MappingIntervention {

    @Autowired
    private InterventionSheetService interventionSheetService;

    public IncidentResponseOneCaserne mappingSinisterSheetOneCaserne(List<Sinister> sinisters){
        List<IncidentSheetData> incidentList = new ArrayList<>();
        Sinister firstSinister = sinisters.get(0);
        for (Sinister element: sinisters) {
            incidentList.add(new IncidentSheetData(element.getId(), element.getOwners(), element.getVictims(), element.getDamages(), element.getComments()));
        }
        return new IncidentResponseOneCaserne(firstSinister.getInterventionSheet().getKey().getIntervention().getId(), firstSinister.getInterventionSheet().getKey().getIntervention().getDate(), firstSinister.getInterventionSheet().getKey().getIntervention().getCaller(), firstSinister.getInterventionSheet().getKey().getIntervention().getIncident(), firstSinister.getInterventionSheet().getKey().getCaserne().getId(), firstSinister.getInterventionSheet().getKey().getCaserne().getName(), incidentList);
    }

    public IncidentResponseMultiCaserne mappingSinisterSheetMultiCaserne(List<Sinister> sinisters){
        List<IncidentSheetData> incidentList = new ArrayList<>();
        List<IncidentSheetCaserneData> caserneDataList = new ArrayList<>();
        Sinister firstSinister = sinisters.get(0);
        int tmpCaserne = -1;
        for (Sinister element: sinisters) {
            if(tmpCaserne == -1){
                tmpCaserne = element.getInterventionSheet().getKey().getCaserne().getId();
            }
            else if (element.getInterventionSheet().getKey().getCaserne().getId() != tmpCaserne) {
                caserneDataList.add(new IncidentSheetCaserneData(element.getInterventionSheet().getKey().getCaserne().getId(), element.getInterventionSheet().getKey().getCaserne().getName(), incidentList));
                tmpCaserne = element.getInterventionSheet().getKey().getCaserne().getId();
                incidentList.clear();
            }

            incidentList.add(new IncidentSheetData(element.getId(), element.getOwners(), element.getVictims(), element.getDamages(), element.getComments()));
        }
        Sinister lastSinister = sinisters.get(sinisters.size() - 1);
        caserneDataList.add(new IncidentSheetCaserneData(lastSinister.getInterventionSheet().getKey().getCaserne().getId(), lastSinister.getInterventionSheet().getKey().getCaserne().getName(), incidentList));

        return new IncidentResponseMultiCaserne(firstSinister.getId(), firstSinister.getCreateAt(), firstSinister.getInterventionSheet().getKey().getIntervention().getCaller(), firstSinister.getInterventionSheet().getKey().getIntervention().getIncident(), caserneDataList);
    }

    public List<MessageResponse> mappingMessage(List<InterventionSheetToMessage> messages){
        List<MessageResponse> response = new ArrayList<>();
        for (InterventionSheetToMessage element: messages) {
            response.add(new MessageResponse(element.getId(), element.getInterventionSheet().getKey().getIntervention().getId(), element.getInterventionSheet().getKey().getCaserne().getId(), element.getEquipe().getId(), element.getSentAt(), element.getMessage()));
        }
        return response;
    }

    public Optional<InterventionResponse> mappingIntervention(List<InterventionSheet> interventionSheets, Optional<Intervention> targetIntervention){
        if(interventionSheets.size() == 0){
            if(targetIntervention.isEmpty()) return Optional.empty();
            else{
                InterventionResponse interventionResponse = new InterventionResponse(targetIntervention.get().getId(), targetIntervention.get().getDate().toLocalDate(), targetIntervention.get().getDate().toLocalTime(), targetIntervention.get().getCaller().getProvenance(), targetIntervention.get().getCaller().getPhoneNumber(), targetIntervention.get().getCaller().getName(), targetIntervention.get().getCaller().getAddress(), targetIntervention.get().getCaller().getLocalisation().getLongitude(), targetIntervention.get().getCaller().getLocalisation().getLatitude(), targetIntervention.get().getCaller().getLocalisation().getPrecision(), targetIntervention.get().getIncident(), targetIntervention.get().getStatus(), new ArrayList<>());
                return Optional.of(interventionResponse);
            }
        }
        else{
            List<InterventionSheetToTeam> teams;
            List<InterventionTeamData> teamData = new ArrayList<>();
            Optional<InterventionTeamEnginData> enginData;
            List<InterventionTeamMemberData> memberData = new ArrayList<>();
            Intervention intervention = interventionSheets.get(0).getKey().getIntervention();
            InterventionResponse interventionResponse = new InterventionResponse(intervention.getId(), intervention.getDate().toLocalDate(), intervention.getDate().toLocalTime(), intervention.getCaller().getProvenance(), intervention.getCaller().getPhoneNumber(), intervention.getCaller().getName(), intervention.getCaller().getAddress(), intervention.getCaller().getLocalisation().getLongitude(), intervention.getCaller().getLocalisation().getLatitude(), intervention.getCaller().getLocalisation().getPrecision(), intervention.getIncident(), intervention.getStatus(), new ArrayList<>());
            List<InterventionCaserneData> casernes = new ArrayList<>();
            for(InterventionSheet element : interventionSheets){
                teams = this.interventionSheetService.findInterventionTeamByInterventionSheet(element);
                for(InterventionSheetToTeam team : teams){
                    enginData = team.getEquipe().getEngin() != null ? Optional.of(new InterventionTeamEnginData(team.getEquipe().getEngin().getId(), team.getEquipe().getEngin().getEnginType().getEnginType().name(), team.getEquipe().getEngin().getImmatriculation())) : Optional.empty();
                    for(DailyTeamMember member: team.getEquipe().getMembers()){
                        memberData.add(new InterventionTeamMemberData(member.getPrincipal().getId(), member.getPrincipal().getMatricule(), member.getPrincipal().getFirstname(), member.getPrincipal().getLastname(), member.getSecondary().getId(), member.getSecondary().getMatricule(), member.getSecondary().getFirstname(), member.getSecondary().getLastname(), member.getFonction().getRule().name()));
                    }
                    teamData.add(new InterventionTeamData(team.getId(), team.getEquipe().getId(), team.getEquipe().getType().getEquipeType().name(), enginData, Optional.ofNullable(team.getPresentation()), Optional.ofNullable(team.getDeparture()), Optional.ofNullable(team.getAvailable()), Optional.ofNullable(team.getCheckIn()), team.getEquipe().isActive(), new ArrayList<>(memberData)));
                    memberData.clear();
                }
                casernes.add(new InterventionCaserneData(element.getKey().getCaserne().getId(), element.getKey().getCaserne().getName(), element.getMessage(), new ArrayList<>(teamData)));
                teamData.clear();
            }
            interventionResponse.setCasernes(casernes);
            return Optional.of(interventionResponse);
        }
    }
}
