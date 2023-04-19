package bf.bnsp.api.tools.mappingTools;

import bf.bnsp.api.account.dto.response.*;
import bf.bnsp.api.account.dto.response.partialData.EnginInfo;
import bf.bnsp.api.account.dto.response.partialData.TeamInfo;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.model.Fonction;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            }
            else if(fonction.getEquipe().getId() != tmpTeam){
                tmpTeamResponse = new FonctionTeamResponse(tmpTeam, fonction.getEquipe().getEquipeType().getEquipeType().name(), fonction.getEquipe().getDesignation(), agentList);
                teamList.add(tmpTeamResponse);
                tmpTeam = fonction.getEquipe().getId();
                agentList.clear();
            }
            tmpAgent = fonction.getKeys().getAgent();
            agentList.add(new FonctionAgentResponse(tmpAgent.getId(), fonction.getFunction().getRule().name(), tmpAgent.getFirstname(), tmpAgent.getLastname(), tmpAgent.getGrade().getGrade().name()));
        }
        Equipe lastTeam = fonctions.get(fonctions.size() - 1).getEquipe();
        tmpTeamResponse = new FonctionTeamResponse(lastTeam.getId(), lastTeam.getEquipeType().getEquipeType().name(), lastTeam.getDesignation(), agentList);
        teamList.add(tmpTeamResponse);
        return new DailyProgramResponse(fonctions.get(0).getKeys().getDate(), fonctions.get(0).getEquipe().getCaserne().getId(), fonctions.get(0).getEquipe().getCaserne().getName(), fonctions.get(0).getEquipe().getCaserne().getCity(), fonctions.get(0).getEquipe().getCaserne().getArea(), teamList);
    }

    public List<TeamResponse> mappingTeam(List<Equipe> equipes){
        List<TeamResponse> teamResponses = new ArrayList<>();
        List<TeamInfo> tmpTeam = new ArrayList<>();
        TeamInfo tmpTeamResponse;
        TeamResponse tmpCaserneResponse;
        int tmpCaserne = -1;
        for(Equipe equipe: equipes){
            if(tmpCaserne == -1){
                tmpCaserne = equipe.getId();
            }
            else if (equipe.getCaserne().getId() != tmpCaserne) {
                tmpCaserneResponse = new TeamResponse(equipe.getCaserne().getId(), equipe.getCaserne().getName(), equipe.getCaserne().getCity(), equipe.getCaserne().getArea(), tmpTeam);
                teamResponses.add(tmpCaserneResponse);
                tmpCaserne = equipe.getId();
                tmpTeam.clear();
            }
            tmpTeamResponse = new TeamInfo(equipe.getId(), equipe.getDesignation(), equipe.getEquipeType().getEquipeType().name(), Optional.empty());
            if(equipe.getEngin() != null) tmpTeamResponse.setEngin(Optional.of(new EnginInfo(equipe.getEngin().getId(), equipe.getEngin().getImmatriculation(), equipe.getEngin().getEnginType().getEnginType().name(), equipe.getEngin().getDescription(), equipe.getEngin().isSortie(), equipe.getEngin().isAvailable())));
            tmpTeam.add(tmpTeamResponse);
        }
        Caserne lastCaserne = equipes.get(equipes.size() - 1).getCaserne();
        tmpCaserneResponse = new TeamResponse(lastCaserne.getId(), lastCaserne.getName(), lastCaserne.getCity(), lastCaserne.getArea(), tmpTeam);
        teamResponses.add(tmpCaserneResponse);
        return teamResponses;
    }

    public List<DailyProgramResponseList> mappingDailyProgramList(List<Fonction> fonctions){
    return null;
    }
}
