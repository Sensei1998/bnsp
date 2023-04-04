package bf.bnsp.api.account.controller;

import bf.bnsp.api.account.dto.form.AgentCreationForm;
import bf.bnsp.api.account.dto.form.AgentUpdateForm;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.service.AgentService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.tools.dataType.EGrade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class AgentController {

    private final AgentService agentService;

    private final CaserneService caserneService;

    public AgentController(AgentService agentService, CaserneService caserneService) {
        this.agentService = agentService;
        this.caserneService = caserneService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@RequestBody AgentCreationForm agentForm){
        Optional<Agent> response;
        if(agentForm.getGradeId() < 1 || agentForm.getGradeId() > EGrade.values().length) return ResponseEntity.notFound().build();
        else{
            Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(agentForm.getCaserneId());
            if(caserne.isEmpty()) return ResponseEntity.notFound().build();
            else{
                response = this.agentService.createAgent(agentForm, caserne.get());
                return new ResponseEntity<>(response.get(), HttpStatus.CREATED);
            }
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateUser(@RequestBody AgentUpdateForm agentForm){
        Optional<Agent> targetedAgent = this.agentService.findActiveAgentById(agentForm.getId());
        Optional<Agent> response;
        if(targetedAgent.isEmpty() || agentForm.getGradeId() < 1 || agentForm.getGradeId() > 3) return ResponseEntity.notFound().build();
        else{
            Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(agentForm.getCaserneId());
            if(caserne.isEmpty()) return ResponseEntity.notFound().build();
            else{
                response = this.agentService.updateAgent(agentForm, caserne.get(), targetedAgent.get());
                return new ResponseEntity<>(response.get(), HttpStatus.CREATED);
            }
        }
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getAllActiveUsers(){
        List<Agent> response = this.agentService.findAllActiveAgent();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getActiveUserById(@PathVariable("id") int id){
        Optional<Agent> response = this.agentService.findActiveAgentById(id);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/caserne/{caserneId}")
    public ResponseEntity<?> getAllActiveUsersByCaserne(@PathVariable("caserneId") int caserneId){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(caserneId);
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<Agent> response = this.agentService.findAllActiveAgentByCaserne(caserne.get());
            return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/grades")
    public ResponseEntity<?> getAllGrades(){
        return new ResponseEntity<>(this.agentService.findAllGrade(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUSerById(@PathVariable("id") int id){
        Optional<Agent> targetedAgent = this.agentService.findActiveAgentById(id);
        Optional<Agent> response;
        if(targetedAgent.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.agentService.deleteAgent(targetedAgent.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
