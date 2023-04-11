package bf.bnsp.api.tools.mappingTools;

import bf.bnsp.api.account.dto.response.DailyProgramResponseList;
import bf.bnsp.api.account.dto.response.DailyProgramResponse;
import bf.bnsp.api.account.dto.response.FonctionAgentResponse;
import bf.bnsp.api.account.dto.response.FonctionTeamResponse;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.model.Fonction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MappingTool {

    public DailyProgramResponse mappingDailyProgram(List<Fonction> fonctions){
        List<FonctionAgentResponse> agentList = new ArrayList<>();
        List<FonctionTeamResponse> teamList = new ArrayList<>();
        int tmpTeam = -1;
        Agent tmpAgent;
        FonctionTeamResponse tmpTeamResponse;

        for (Fonction fonction: fonctions) {
            if(tmpTeam == -1) {
                tmpTeam = fonction.getEquipe().getId();
                tmpAgent = fonction.getKeys().getAgent();
                agentList.add(new FonctionAgentResponse(tmpAgent.getId(), fonction.getFunction().getRule().name(), tmpAgent.getFirstname(), tmpAgent.getLastname(), tmpAgent.getGrade().getGrade().name()));
            }
            else if(fonction.getEquipe().getId() == tmpTeam){
                tmpAgent = fonction.getKeys().getAgent();
                agentList.add(new FonctionAgentResponse(tmpAgent.getId(), fonction.getFunction().getRule().name(), tmpAgent.getFirstname(), tmpAgent.getLastname(), tmpAgent.getGrade().getGrade().name()));
            }
            else{
                tmpTeamResponse = new FonctionTeamResponse(tmpTeam, fonction.getEquipe().getEquipeType().getEquipeType().name(), fonction.getEquipe().getDesignation(), agentList);
                System.out.println(tmpTeamResponse.toString());
                teamList.add(tmpTeamResponse);
                tmpTeam = fonction.getEquipe().getId();
            }
        }
        Equipe lastTeam = fonctions.get(fonctions.size() - 1).getEquipe();
        tmpTeamResponse = new FonctionTeamResponse(lastTeam.getId(), lastTeam.getEquipeType().getEquipeType().name(), lastTeam.getDesignation(), agentList);
        teamList.add(tmpTeamResponse);
        return new DailyProgramResponse(fonctions.get(0).getKeys().getDate(), fonctions.get(0).getEquipe().getCaserne().getId(), fonctions.get(0).getEquipe().getCaserne().getName(), fonctions.get(0).getEquipe().getCaserne().getCity(), fonctions.get(0).getEquipe().getCaserne().getArea(), teamList);
    }

    public List<DailyProgramResponseList> mappingDailyProgramList(List<Fonction> fonctions){
    return null;
    }
}
