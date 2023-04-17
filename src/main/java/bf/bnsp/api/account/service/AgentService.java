package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.AgentCreationForm;
import bf.bnsp.api.account.dto.form.AgentUpdateForm;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.account.repository.AgentRepository;
import bf.bnsp.api.account.repository.FonctionTypeRepository;
import bf.bnsp.api.account.repository.GradeRepository;
import bf.bnsp.api.caserne.model.Caserne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService implements AgentServiceInterface{

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private FonctionTypeRepository fonctionTypeRepository;

    @Override
    public Optional<Agent> createAgent(AgentCreationForm agentForm, Caserne caserne) {
        Optional<Grade> grade = this.gradeRepository.findById(agentForm.getGradeId());
        if(grade.isEmpty()) return Optional.empty();
        else{
            Agent agent = new Agent(agentForm.getMatricule(), agentForm.getFirstname(), agentForm.getLastname(), agentForm.getPassword(), String.join(";", agentForm.getTelephone()), agentForm.getEmail(), caserne, grade.get());
            if(agentForm.getDefaultFonction() != -1) agent.setDefaultFonction(this.fonctionTypeRepository.findById(agentForm.getDefaultFonction()).get());
            this.agentRepository.save(agent);
            return Optional.of(agent);
        }
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
