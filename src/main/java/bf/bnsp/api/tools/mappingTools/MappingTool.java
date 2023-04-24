package bf.bnsp.api.tools.mappingTools;

import bf.bnsp.api.account.dto.response.*;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.model.DailyTeamMember;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Berickal
 */
@Service
public class MappingTool {

   public DailyProgramResponse mappingDailyProgram(DailyProgram dailyProgram){
        List<FonctionAgentResponse> agentList = new ArrayList<>();
        List<FonctionTeamResponse> teamList = new ArrayList<>();
        for (DailyTeam dailyTeam: dailyProgram.getTeams()) {
            if(!dailyProgram.isHidden()){
                for(DailyTeamMember member : dailyTeam.getMembers()){
                    if(!member.isHidden()) agentList.add(new FonctionAgentResponse(member.getId(), member.getPrincipal().getId(), member.getPrincipal().getFirstname(), member.getPrincipal().getLastname(), member.getPrincipal().getGrade().getGrade().name(), member.getSecondary().getId(), member.getSecondary().getFirstname(), member.getSecondary().getLastname(), member.getSecondary().getGrade().getGrade().name(), member.getFonction().getRule().name()));
                }
                teamList.add(new FonctionTeamResponse(dailyTeam.getId(), dailyTeam.getType().getEquipeType().name(), dailyTeam.getDesignation(), new ArrayList<>(agentList)));
                agentList.clear();
            }
        }
        return new DailyProgramResponse(dailyProgram.getId(), dailyProgram.getDate(), dailyProgram.getCaserne().getId(), dailyProgram.getCaserne().getName(), dailyProgram.getCaserne().getCity(), dailyProgram.getCaserne().getArea(), teamList);
    }

    public DailyProgramMinResponse mappingDailyProgramMin(DailyProgram dailyProgram){
        return new DailyProgramMinResponse(dailyProgram.getId(), dailyProgram.getDate(), dailyProgram.getCaserne().getId(), dailyProgram.getCaserne().getName(), dailyProgram.getCaserne().getCity(), dailyProgram.getCaserne().getArea());
    }

    public FonctionTeamResponse mappingDailyTeam(DailyTeam dailyTeam){
       List<FonctionAgentResponse> agentList = new ArrayList<>();
       for(DailyTeamMember member : dailyTeam.getMembers()){
           if(!member.isHidden()) agentList.add(new FonctionAgentResponse(member.getId(), member.getPrincipal().getId(), member.getPrincipal().getFirstname(), member.getPrincipal().getLastname(), member.getPrincipal().getGrade().getGrade().name(), member.getSecondary().getId(), member.getSecondary().getFirstname(), member.getSecondary().getLastname(), member.getSecondary().getGrade().getGrade().name(), member.getFonction().getRule().name()));
        }
        return new FonctionTeamResponse(dailyTeam.getId(), dailyTeam.getType().getEquipeType().name(), dailyTeam.getDesignation(), agentList);
    }

}
