package bf.bnsp.api.tools.mappingTools;

import bf.bnsp.api.account.dto.response.*;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.model.DailyTeamMember;
import bf.bnsp.api.account.model.EquipeType;
import bf.bnsp.api.account.repository.DailyTeamRepository;
import bf.bnsp.api.account.repository.EquipeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author Berickal
 */
@Service
public class MappingTool {

    @Autowired
    DailyTeamRepository dailyTeamRepository;

    @Autowired
    EquipeTypeRepository equipeTypeRepository;

   public DailyProgramResponse mappingDailyProgram(DailyProgram dailyProgram){
        List<FonctionAgentResponse> agentList = new ArrayList<>();
        List<FonctionTeamResponse> teamList = new ArrayList<>();
        for (DailyTeam dailyTeam: dailyProgram.getTeams()) {
            if(!dailyProgram.isHidden()){
                for(DailyTeamMember member : dailyTeam.getMembers()){
                    if(!member.isHidden()) agentList.add(new FonctionAgentResponse(member.getId(), member.getPrincipal().getId(), member.getPrincipal().getFirstname(), member.getPrincipal().getLastname(), member.getPrincipal().getGrade().getGrade().name(), member.getSecondary().getId(), member.getSecondary().getFirstname(), member.getSecondary().getLastname(), member.getSecondary().getGrade().getGrade().name(), member.getFonction().getRule().name()));
                }
                teamList.add(new FonctionTeamResponse(dailyTeam.getId(), dailyTeam.getType().getEquipeType(), dailyTeam.getDesignation(), new ArrayList<>(agentList), dailyTeam.isActive()));
                agentList.clear();
            }
        }
        return new DailyProgramResponse(dailyProgram.getId(), dailyProgram.getDate(), dailyProgram.getCaserne().getId(), dailyProgram.getCaserne().getName(), dailyProgram.getCaserne().getCity().getZone(), dailyProgram.getCaserne().getArea(), teamList);
    }

    public DailyProgramMinResponse mappingDailyProgramMin(DailyProgram dailyProgram){
        Optional<EquipeType> caporal = this.equipeTypeRepository.findById(3);
        Optional<EquipeType> sgt = this.equipeTypeRepository.findById(4);
        Optional<DailyTeam> caporalTeam = this.dailyTeamRepository.findByDateAndTypeAndHiddenFalse(dailyProgram.getDate(), caporal.get());
        Optional<DailyTeam> sgtTeam = this.dailyTeamRepository.findByDateAndTypeAndHiddenFalse(dailyProgram.getDate(), sgt.get());
        Optional<FonctionTeamResponse> caporalTeamResponse = Optional.empty();
        Optional<FonctionTeamResponse> sgtTeamResponse = Optional.empty();
        List<FonctionAgentResponse> agentList = new ArrayList<>();
        if(caporalTeam.get().getMembers().size() > 0){
            for(DailyTeamMember member : caporalTeam.get().getMembers()){
                if(!member.isHidden()) agentList.add(new FonctionAgentResponse(member.getId(), member.getPrincipal().getId(), member.getPrincipal().getFirstname(), member.getPrincipal().getLastname(), member.getPrincipal().getGrade().getGrade().name(), member.getSecondary().getId(), member.getSecondary().getFirstname(), member.getSecondary().getLastname(), member.getSecondary().getGrade().getGrade().name(), member.getFonction().getRule().name()));
            }
            caporalTeamResponse = Optional.of(new FonctionTeamResponse(caporalTeam.get().getId(), caporalTeam.get().getType().getEquipeType(), caporalTeam.get().getDesignation(), new ArrayList<>(agentList), caporalTeam.get().isActive()));
        }
        agentList.clear();
        if(sgtTeam.get().getMembers().size() > 0){
            for(DailyTeamMember member : sgtTeam.get().getMembers()){
                if(!member.isHidden()) agentList.add(new FonctionAgentResponse(member.getId(), member.getPrincipal().getId(), member.getPrincipal().getFirstname(), member.getPrincipal().getLastname(), member.getPrincipal().getGrade().getGrade().name(), member.getSecondary().getId(), member.getSecondary().getFirstname(), member.getSecondary().getLastname(), member.getSecondary().getGrade().getGrade().name(), member.getFonction().getRule().name()));
            }
            sgtTeamResponse = Optional.of(new FonctionTeamResponse(sgtTeam.get().getId(), sgtTeam.get().getType().getEquipeType(), caporalTeam.get().getDesignation(), new ArrayList<>(agentList), sgtTeam.get().isActive()));
        }
        return new DailyProgramMinResponse(dailyProgram.getId(), dailyProgram.getDate(), dailyProgram.getCaserne().getId(), dailyProgram.getCaserne().getName(), dailyProgram.getCaserne().getCity().getZone(), dailyProgram.getCaserne().getArea(), caporalTeamResponse, sgtTeamResponse);
    }

    public FonctionTeamResponse mappingDailyTeam(DailyTeam dailyTeam){
       List<FonctionAgentResponse> agentList = new ArrayList<>();
       for(DailyTeamMember member : dailyTeam.getMembers()){
           if(!member.isHidden()) agentList.add(new FonctionAgentResponse(member.getId(), member.getPrincipal().getId(), member.getPrincipal().getFirstname(), member.getPrincipal().getLastname(), member.getPrincipal().getGrade().getGrade().name(), member.getSecondary().getId(), member.getSecondary().getFirstname(), member.getSecondary().getLastname(), member.getSecondary().getGrade().getGrade().name(), member.getFonction().getRule().name()));
        }
        return new FonctionTeamResponse(dailyTeam.getId(), dailyTeam.getType().getEquipeType(), dailyTeam.getDesignation(), agentList, dailyTeam.isActive());
    }

}
