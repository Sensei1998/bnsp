package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.AgentCreationForm;
import bf.bnsp.api.account.dto.form.AgentLoginForm;
import bf.bnsp.api.account.dto.form.AgentUpdateForm;
import bf.bnsp.api.account.dto.response.AgentLoginResponse;
import bf.bnsp.api.account.dto.response.partialData.LoginAgentInfo;
import bf.bnsp.api.account.model.*;
import bf.bnsp.api.account.repository.*;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.security.JwtUtils;
import bf.bnsp.api.security.PasswordEncryption;
import bf.bnsp.api.security.UserDetailsImpl;
import bf.bnsp.api.tools.dataType.EFonction;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Service
@AllArgsConstructor
public class AgentService implements AgentServiceInterface{

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private FonctionTypeRepository fonctionTypeRepository;

    @Autowired
    private DailyTeamMemberRepository dailyTeamMemberRepository;

    JwtUtils jwtUtils;

    AuthenticationManager authenticationManager;

    private PasswordEncryption encryption;

    @Override
    public Optional<Agent> createAgent(AgentCreationForm agentForm, Caserne caserne) {
        Optional<Grade> grade = this.gradeRepository.findById(agentForm.getGradeId());
        if(grade.isEmpty()) return Optional.empty();
        else{
            Agent agent = new Agent(agentForm.getMatricule(), agentForm.getFirstname(), agentForm.getLastname(), encryption.encode(agentForm.getPassword()), String.join(";", agentForm.getTelephone()), agentForm.getEmail(), caserne, grade.get());
            if(agentForm.getDefaultFonction() != -1) agent.setDefaultFonction(this.fonctionTypeRepository.findById(agentForm.getDefaultFonction()).get());
            this.agentRepository.save(agent);
            return Optional.of(agent);
        }
    }

    @Override
    public Optional<AgentLoginResponse> loginAgent(AgentLoginForm loginForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getIdentifier(), loginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        if(!authentication.isAuthenticated() || user == null){
            return Optional.empty();
        }
        Optional<DailyTeamMember> ruleFromDailyProgram = this.dailyTeamMemberRepository.findByAgentAndDateAndHiddenFalse(user.getUser(), LocalDate.now());
        EFonction agentRule = ruleFromDailyProgram.isPresent() ? ruleFromDailyProgram.get().getFonction().getRule() : user.getUser().getDefaultFonction().getRule();

        AgentLoginResponse response = new AgentLoginResponse("Success", token, Optional.of(new LoginAgentInfo(user.getUser().getId(), user.getUser().getMatricule(), user.getUser().getFirstname(), user.getUser().getLastname(), user.getUser().getEmail(), List.of(user.getUser().getPhoneNumber().split(";")), user.getUser().getGrade().getGrade().name(), agentRule.name(), user.getUser().getCaserne().getId())));
        return Optional.of(response);
    }

    @Override
    public Optional<Agent> updateAgent(AgentUpdateForm agentForm, Caserne caserne, Agent targetedAgent) {
        Optional<Grade> grade = this.gradeRepository.findById(agentForm.getGradeId());
        if(grade.isEmpty()) return Optional.empty();
        else{
            targetedAgent.setMatricule(agentForm.getMatricule());
            targetedAgent.setFirstname(agentForm.getFirstname());
            targetedAgent.setLastname(agentForm.getLastname());
            targetedAgent.setCaserne(caserne);
            targetedAgent.setGrade(grade.get());
            targetedAgent.setEmail(agentForm.getEmail());
            targetedAgent.setPhoneNumber(String.join(";", agentForm.getTelephone()));
            if(agentForm.getDefaultFonction() != -1) targetedAgent.setDefaultFonction(this.fonctionTypeRepository.findById(agentForm.getDefaultFonction()).get());
            this.agentRepository.save(targetedAgent);
            return Optional.of(targetedAgent);
        }
    }

    @Override
    public Optional<Agent> findActiveAgentById(int agentId) {
        return this.agentRepository.findByIdAndHiddenFalse(agentId);
    }

    @Override
    public Optional<Agent> findActiveAgentByIdAndCasernce(int agentId, Caserne caserne) {
        return this.agentRepository.findByCaserneAndIdAndHiddenFalse(caserne, agentId);
    }

    @Override
    public List<Agent> findAllActiveAgent() {
        return this.agentRepository.findByHiddenFalse();
    }

    @Override
    public List<Agent> findAllActiveAgentByCaserne(Caserne caserne) {
        return this.agentRepository.findByCaserneAndHiddenFalse(caserne);
    }

    @Override
    public List<Grade> findAllGrade() {
        return this.gradeRepository.findAll();
    }

    @Override
    public Optional<Agent> deleteAgent(Agent targetedAgent) {
        targetedAgent.setHidden(true);
        targetedAgent.setMatricule(targetedAgent.getMatricule() + "_#HIDDEN" + this.agentRepository.countByMatriculeContains(targetedAgent.getMatricule()));
        targetedAgent.setEmail(targetedAgent.getEmail() + "_#HIDDEN" + this.agentRepository.countByEmailContains(targetedAgent.getEmail()));
        this.agentRepository.save(targetedAgent);
        return Optional.of(targetedAgent);
    }
}
