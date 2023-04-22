package bf.bnsp.api.tools.mappingTools;

import bf.bnsp.api.account.dto.response.*;
import bf.bnsp.api.account.dto.response.partialData.EnginInfo;
import bf.bnsp.api.account.dto.response.partialData.TeamInfo;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.model.DailyTeamMember;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MappingTool {

   public DailyProgramResponse mappingDailyProgram(DailyProgram dailyProgram){
        List<FonctionAgentResponse> agentList = new ArrayList<>();
        List<FonctionTeamResponse> teamList = new ArrayList<>();
        for (DailyTeam dailyTeam: dailyProgram.getTeams()) {
            for(DailyTeamMember member : dailyTeam.getMembers()){
                agentList.add(new FonctionAgentResponse(member.getId(), member.getAgent().getId(), member.getFonction().getRule().name(), member.getAgent().getFirstname(), member.getAgent().getLastname(), member.getAgent().getGrade().getGrade().name()));
            }
            teamList.add(new FonctionTeamResponse(dailyTeam.getId(), dailyTeam.getType().getEquipeType().name(), dailyTeam.getDesignation(), new ArrayList<>(agentList)));
            agentList.clear();
        }
        return new DailyProgramResponse(dailyProgram.getDate(), dailyProgram.getCaserne().getId(), dailyProgram.getCaserne().getName(), dailyProgram.getCaserne().getCity(), dailyProgram.getCaserne().getArea(), teamList);
    }

    public FonctionTeamResponse mappingDailyTeam(DailyTeam dailyTeam){
       List<FonctionAgentResponse> agentList = new ArrayList<>();
       for(DailyTeamMember member : dailyTeam.getMembers()){
            agentList.add(new FonctionAgentResponse(member.getId(), member.getAgent().getId(), member.getFonction().getRule().name(), member.getAgent().getFirstname(), member.getAgent().getLastname(), member.getAgent().getGrade().getGrade().name()));
        }
        return new FonctionTeamResponse(dailyTeam.getId(), dailyTeam.getType().getEquipeType().name(), dailyTeam.getDesignation(), agentList);
    }

     /*public List<TeamResponse> mappingTeam(List<Equipe> equipes){
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
    }*/
}
