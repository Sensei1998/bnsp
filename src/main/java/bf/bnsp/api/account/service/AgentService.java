package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.AgentCreationForm;
import bf.bnsp.api.account.dto.form.AgentUpdateForm;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.account.repository.AgentRepository;
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

    @Override
    public Optional<Agent> createAgent(AgentCreationForm agentForm, Caserne caserne) {
        Optional<Grade> grade = this.gradeRepository.findById(agentForm.getGradeId());
        if(grade.isEmpty()) return Optional.empty();
        else{
            Agent agent = new Agent(agentForm.getMatricule(), agentForm.getFirstname(), agentForm.getLastname(), agentForm.getPassword(), caserne, grade.get());
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
        this.agentRepository.save(targetedAgent);
        return Optional.of(targetedAgent);
    }
}
