package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.DailyProgramCreationForm;
import bf.bnsp.api.account.dto.form.DailyProgramInitForm;
import bf.bnsp.api.account.dto.form.DailyTeamAddForm;
import bf.bnsp.api.account.dto.form.DailyTeamUpdateForm;
import bf.bnsp.api.account.dto.form.partialData.DailyMemberForm;
import bf.bnsp.api.account.dto.form.partialData.DailyTeamForm;
import bf.bnsp.api.account.model.*;
import bf.bnsp.api.account.repository.*;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.caserne.service.EnginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Service
public class DailyProgramService implements DailyProgramServiceInterface{

    @Autowired
    private DailyProgramRepository dailyProgramRepository;

    @Autowired
    private DailyTeamRepository dailyTeamRepository;

    @Autowired
    private DailyTeamMemberRepository dailyTeamMemberRepository;

    @Autowired
    private FonctionTypeRepository fonctionTypeRepository;

    @Autowired
    private EquipeTypeRepository equipeTypeRepository;

    @Autowired
    private AgentService agentService;

    @Autowired
    private EnginService enginService;


    @Override
    public Optional<DailyProgram> createDailyProgram(DailyProgramInitForm programForm, Caserne caserne) {
        List<DailyTeamMember> members = new ArrayList<>();
        List<DailyTeam> teams = new ArrayList<>();
        Optional<Agent> tmpAgentMain;
        Optional<Agent> tmpAgentSecond;
        Optional<FonctionType> tmpFonctionType;
        Optional<EquipeType> tmpTeamType;
        Optional<Engin> tmpEngin;
        DailyTeam tmpTeam;
        DailyTeamMember tmpMember;
        for (DailyTeamForm teamForm: programForm.getEquipes()) {
            tmpTeamType = this.equipeTypeRepository.findById(teamForm.getTypeId());
            if(tmpTeamType.isEmpty()) return Optional.empty();
            for (DailyMemberForm memberForm: teamForm.getMembers()) {
                tmpAgentMain = this.agentService.findActiveAgentByIdAndCasernce(memberForm.getPrincipalId(), caserne);
                tmpAgentSecond = this.agentService.findActiveAgentByIdAndCasernce(memberForm.getRemplacantId(), caserne);
                tmpFonctionType = this.fonctionTypeRepository.findById(memberForm.getFonctionId());
                if(tmpAgentMain.isEmpty() || tmpFonctionType.isEmpty() || tmpAgentSecond.isEmpty()) return Optional.empty();
                tmpMember = new DailyTeamMember(programForm.getDate(), tmpAgentMain.get(), tmpAgentSecond.get(), tmpFonctionType.get());
                members.add(tmpMember);

            }
            tmpTeam = new DailyTeam(programForm.getDate(), tmpTeamType.get(), teamForm.getDesignation());
            tmpTeam.setMembers(new ArrayList<>(members));
            if(teamForm.getEnginId().isPresent()){
                if(teamForm.getEnginId().get() != -1){
                    tmpEngin = this.enginService.findActiveEnginByCaserneAndId(teamForm.getEnginId().get(), caserne);
                    if(tmpEngin.isEmpty()) return Optional.empty();
                    else tmpTeam.setEngin(tmpEngin.get());
                }
            }
            teams.add(tmpTeam);
            members.clear();
        }
        DailyProgram dailyProgram = new DailyProgram(caserne, programForm.getDate());
        dailyProgram.setTeams(teams);
        this.dailyProgramRepository.save(dailyProgram);
        return Optional.of(dailyProgram);
    }

    @Override
    public Optional<DailyProgram> createDailyProgramWithMainAgent(DailyProgramCreationForm programForm, Caserne caserne) {
        List<DailyTeamMember> members = new ArrayList<>();
        List<DailyTeam> teams = new ArrayList<>();
        Optional<Agent> tmpAgentMain;
        Optional<Agent> tmpAgentSecond;
        DailyTeam tmpTeam;
        DailyTeamMember tmpMember;
        if(programForm.getCaporal().isPresent() && programForm.getSergent().isPresent()){
            tmpAgentMain = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getPrincipal(), caserne);
            tmpAgentSecond = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getRemplacant(), caserne);
            if(tmpAgentMain.isEmpty() || tmpAgentSecond.isEmpty()) return Optional.empty();
            else members.add(new DailyTeamMember(programForm.getDate(), tmpAgentMain.get(), tmpAgentSecond.get(), this.fonctionTypeRepository.findById(5).get()));

            tmpAgentMain = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getPrincipal(), caserne);
            tmpAgentSecond = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getRemplacant(), caserne);
            if(tmpAgentMain.isEmpty() || tmpAgentSecond.isEmpty()) return Optional.empty();
            else {
                tmpMember = new DailyTeamMember(programForm.getDate(), tmpAgentMain.get(), tmpAgentSecond.get(), this.fonctionTypeRepository.findById(5).get());
                members.add(tmpMember);
            }
            tmpTeam = new DailyTeam(programForm.getDate(), this.equipeTypeRepository.findById(3).get(), "Caporal du jour");
            tmpTeam.setMembers(new ArrayList<>(members));
            teams.add(tmpTeam);
            members.clear();

            tmpAgentMain = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getPrincipal(), caserne);
            tmpAgentSecond = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getRemplacant(), caserne);
            if(tmpAgentMain.isEmpty() || tmpAgentSecond.isEmpty()) return Optional.empty();
            else members.add(new DailyTeamMember(programForm.getDate(), tmpAgentMain.get(), tmpAgentSecond.get(), this.fonctionTypeRepository.findById(6).get()));

            tmpAgentMain = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getPrincipal(), caserne);
            tmpAgentSecond = this.agentService.findActiveAgentByIdAndCasernce(programForm.getCaporal().get().getRemplacant(), caserne);
            if(tmpAgentMain.isEmpty() || tmpAgentSecond.isEmpty()) return Optional.empty();
            else {
                tmpMember = new DailyTeamMember(programForm.getDate(), tmpAgentMain.get(), tmpAgentSecond.get(), this.fonctionTypeRepository.findById(6).get());
                members.add(tmpMember);
            }
            tmpTeam = new DailyTeam(programForm.getDate(), this.equipeTypeRepository.findById(3).get(), "Sergent du jour");
            tmpTeam.setMembers(new ArrayList<>(members));
            teams.add(tmpTeam);

            DailyProgram dailyProgram = new DailyProgram(caserne, programForm.getDate());
            dailyProgram.setTeams(teams);
            this.dailyProgramRepository.save(dailyProgram);
            return Optional.of(dailyProgram);
        }
        else{
            DailyProgram dailyProgram = new DailyProgram(caserne, programForm.getDate());
            this.dailyProgramRepository.save(dailyProgram);
            return Optional.of(dailyProgram);
        }
    }

    @Override
    public Optional<DailyProgram> addTeamToDailyProgram(DailyTeamAddForm teamForm, DailyProgram dailyProgram) {
        List<DailyTeamMember> members = new ArrayList<>();
        Optional<Agent> tmpAgentMain;
        Optional<Agent> tmpAgentSecond;
        Optional<FonctionType> tmpFonctionType;
        Optional<EquipeType> tmpTeamType;
        Optional<Engin> tmpEngin;
        DailyTeam tmpTeam;
        DailyTeamMember tmpMember;

        tmpTeamType = this.equipeTypeRepository.findById(teamForm.getTypeId());
        if(tmpTeamType.isEmpty()) return Optional.empty();
        for (DailyMemberForm memberForm: teamForm.getMembers()) {
            tmpAgentMain = this.agentService.findActiveAgentByIdAndCasernce(memberForm.getPrincipalId(), dailyProgram.getCaserne());
            tmpAgentSecond = this.agentService.findActiveAgentByIdAndCasernce(memberForm.getRemplacantId(), dailyProgram.getCaserne());
            tmpFonctionType = this.fonctionTypeRepository.findById(memberForm.getFonctionId());
            if(tmpAgentMain.isEmpty() || tmpFonctionType.isEmpty() || tmpAgentSecond.isEmpty()) return Optional.empty();

            tmpMember = new DailyTeamMember(dailyProgram.getDate(), tmpAgentMain.get(), tmpAgentSecond.get(), tmpFonctionType.get());
            members.add(tmpMember);
        }

        tmpTeam = new DailyTeam(dailyProgram.getDate(), tmpTeamType.get(), teamForm.getDesignation());
        tmpTeam.setMembers(new ArrayList<>(members));
        if(teamForm.getEnginId().isPresent()){
            if(teamForm.getEnginId().get() != -1){
                tmpEngin = this.enginService.findActiveEnginByCaserneAndId(teamForm.getEnginId().get(), dailyProgram.getCaserne());
                if(tmpEngin.isEmpty()) return Optional.empty();
                else tmpTeam.setEngin(tmpEngin.get());
            }
        }
        List<DailyTeam> registeredTeam = dailyProgram.getTeams();
        registeredTeam.add(tmpTeam);
        dailyProgram.setTeams(registeredTeam);
        this.dailyProgramRepository.save(dailyProgram);
        return Optional.of(dailyProgram);
    }

    @Override
    public Optional<DailyTeam> updateTeamComposition(DailyTeamUpdateForm teamForm, DailyTeam dailyTeam) {
        List<Integer> registeredAgentId = new ArrayList<>();
        List<Integer> formAgentId = new ArrayList<>();
        List<Integer> deletedList;
        List<Integer> addedList;
        List<Integer> updatedList;
        Optional<Agent> tmpAgentMain;
        Optional<Agent> tmpAgentSecond;
        Optional<FonctionType> tmpFonctionType;
        Optional<DailyTeamMember> targetedMember;
        DailyTeamMember tmpMember;

        int index;

        for(DailyTeamMember element: dailyTeam.getMembers()){
            if(!element.isHidden()) {
                registeredAgentId.add(element.getPrincipal().getId());
                registeredAgentId.add(element.getSecondary().getId());
            }
        }
        Optional<Integer> tmpDeletedDailyMember;
        for (DailyMemberForm member: teamForm.getMembers()) {
            formAgentId.add(member.getPrincipalId());
            formAgentId.add(member.getRemplacantId());
        }

        List<DailyTeamMember> dailyMembers = dailyTeam.getMembers();
        deletedList = new ArrayList<>(registeredAgentId);
        deletedList.removeAll(formAgentId);
        addedList = new ArrayList<>(formAgentId);
        addedList.removeAll(registeredAgentId);
        updatedList = new ArrayList<>(formAgentId);
        updatedList.retainAll(registeredAgentId);


        for (int element: deletedList) {
            tmpDeletedDailyMember = this.dailyTeamRepository.findActiveDailyMemberIdByDailyTeamIdAndAgentId(teamForm.getTeamId(), element);
            if(tmpDeletedDailyMember.isEmpty()) return Optional.empty();

            else{
                Optional<DailyTeamMember> deletedElement= this.dailyTeamMemberRepository.findByIdAndHiddenFalse(tmpDeletedDailyMember.get());
                if(deletedElement.get().isActive()){
                    index = dailyMembers.indexOf(deletedElement.get());
                    deletedElement.get().setHidden(true);
                    dailyMembers.set(index, deletedElement.get());
                }
                else dailyMembers.remove(deletedElement.get());
            }
        }
        dailyTeam.setMembers(dailyMembers);
        this.dailyTeamRepository.save(dailyTeam);
        dailyTeam = this.dailyTeamRepository.findById(dailyTeam.getId()).get();
        dailyMembers = dailyTeam.getMembers();

        for(DailyMemberForm member : teamForm.getMembers()){
            if(addedList.contains(member.getPrincipalId()) || addedList.contains(member.getRemplacantId())){
                tmpAgentMain = this.agentService.findActiveAgentById(member.getPrincipalId());
                tmpAgentSecond = this.agentService.findActiveAgentById(member.getRemplacantId());
                tmpFonctionType = this.fonctionTypeRepository.findById(member.getFonctionId());
                if(tmpAgentMain.isEmpty() || tmpAgentSecond.isEmpty() || tmpFonctionType.isEmpty()) return Optional.empty();
                else {
                    tmpMember = new DailyTeamMember(dailyTeam.getDate(), tmpAgentMain.get(), tmpAgentSecond.get(), tmpFonctionType.get());
                    dailyMembers.add(tmpMember);
                }
            }
            else if(updatedList.contains(member.getPrincipalId()) || updatedList.contains((member.getRemplacantId()))){
                targetedMember = this.dailyTeamRepository.findActiveDailyMemberByDailyTeamIdAndAgentId(dailyTeam.getId(), member.getPrincipalId());
                index = dailyMembers.indexOf(targetedMember.get());
                tmpFonctionType = this.fonctionTypeRepository.findById(member.getFonctionId());
                if(tmpFonctionType.isEmpty()) return Optional.empty();
                targetedMember.get().setFonction(tmpFonctionType.get());
                dailyMembers.set(index, targetedMember.get());
            }
        }

        dailyTeam.setMembers(dailyMembers);
        this.dailyTeamRepository.save(dailyTeam);
        return Optional.of(dailyTeam);
    }

    @Override
    public Optional<DailyTeam> findActiveDailyTeamById(long id) {
        return this.dailyTeamRepository.findByIdAndHiddenFalse(id);
    }

    @Override
    public Optional<DailyTeam> deleteDailyTeam(DailyTeam dailyTeam) {
        if(!dailyTeam.isActive()){
            dailyTeam.setMembers(new ArrayList<>());
            this.dailyTeamRepository.save(dailyTeam);
        }
        else{
            List<DailyTeamMember> dailyTeamMembers = dailyTeam.getMembers();
            for (DailyTeamMember element : dailyTeamMembers){
                element.setHidden(true);
            }
            dailyTeam.setMembers(dailyTeamMembers);
            dailyTeam.setHidden(true);
            this.dailyTeamRepository.save(dailyTeam);
        }
        return Optional.of(dailyTeam);
    }

    @Override
    public Optional<DailyProgram> findActiveDailyProgramById(long id) {
        return this.dailyProgramRepository.findByIdAndHiddenFalse(id);
    }

    @Override
    public List<DailyProgram> findAllActiveDailyProgram() {
        return this.dailyProgramRepository.findByHiddenFalse();
    }

    @Override
    public List<DailyProgram> findAllActiveDailyProgramByCaserne(Caserne caserne) {
        return this.dailyProgramRepository.findByCaserneAndHiddenFalse(caserne);
    }

    @Override
    public Optional<DailyProgram> findActiveDailyProgramByDateAndCaserne(LocalDate date, Caserne caserne) {
        return this.dailyProgramRepository.findByDateAndCaserneAndHiddenFalse(date, caserne);
    }

    @Override
    public Optional<FonctionType> findCurrentFonctionByAgent(Agent agent) {
        Optional<DailyTeamMember> response = this.dailyTeamMemberRepository.findByPrincipalOrSecondaryAndDateAndHiddenFalse(agent, agent, LocalDate.now());
        return response.isPresent() ? Optional.of(response.get().getFonction()) : Optional.empty();
    }

    private Optional<DailyTeamMember> findActiveDailyMemberById(int dailyTeamMemberId){
        return this.dailyTeamMemberRepository.findByIdAndHiddenFalse(dailyTeamMemberId);
    }

    private boolean deleteDailyMemberById(int dailyTeamMemberId){
        Optional<DailyTeamMember> targetedMember = this.findActiveDailyMemberById(dailyTeamMemberId);
        if(targetedMember.isEmpty()) return false;
        else {
            if(targetedMember.get().isActive()){
                targetedMember.get().setHidden(true);
                this.dailyTeamMemberRepository.save(targetedMember.get());
            }
            else this.dailyTeamMemberRepository.delete(targetedMember.get());
        }
        return true;
    }
}
