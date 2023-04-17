package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.AgentCreationForm;
import bf.bnsp.api.account.dto.form.AgentLoginForm;
import bf.bnsp.api.account.dto.form.AgentUpdateForm;
import bf.bnsp.api.account.dto.response.AgentLoginResponse;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.caserne.model.Caserne;

import java.util.List;
import java.util.Optional;

public interface AgentServiceInterface {

    Optional<Agent> createAgent(AgentCreationForm agentForm, Caserne caserne);

    Optional<AgentLoginResponse> loginAgent(AgentLoginForm loginForm);

    Optional<Agent> updateAgent(AgentUpdateForm agentForm, Caserne caserne, Agent targetedAgent);

    Optional<Agent> findActiveAgentById(int agentId);

    Optional<Agent> findActiveAgentByIdAndCasernce(int agentId, Caserne caserne);

    List<Agent> findAllActiveAgent();

    List<Agent> findAllActiveAgentByCaserne(Caserne caserne);

    List<Grade> findAllGrade();

    Optional<Agent> deleteAgent(Agent targetedAgent);

}
