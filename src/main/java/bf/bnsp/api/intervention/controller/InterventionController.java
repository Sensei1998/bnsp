package bf.bnsp.api.intervention.controller;

import bf.bnsp.api.account.dto.form.AgentCreationForm;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.service.AgentService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.dto.form.InterventionInitAdvancedForm;
import bf.bnsp.api.intervention.dto.form.InterventionInitForm;
import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.service.InterventionService;
import bf.bnsp.api.tools.dataType.EGrade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/intervention")
@CrossOrigin
public class InterventionController {

    private final AgentService agentService;

    private final InterventionService interventionService;

    public InterventionController(AgentService agentService, InterventionService interventionService) {
        this.agentService = agentService;
        this.interventionService = interventionService;
    }

    @PostMapping(value = "/create/partial")
    public ResponseEntity<?> createPartialIntervention(@RequestBody InterventionInitForm interventionForm){
        Optional<Intervention> response;
        Optional<Agent> agent = this.agentService.findActiveAgentById(interventionForm.getCctoId());
        if(agent.isEmpty()) return ResponseEntity.notFound().build();
        else {
            response = this.interventionService.createBasicIntervention(interventionForm, agent.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/create/complete")
    public ResponseEntity<?> createCompleteIntervention(@RequestBody InterventionInitAdvancedForm interventionForm){
        Optional<Intervention> response;
        Optional<Agent> agent = this.agentService.findActiveAgentById(interventionForm.getCctoId());
        if(agent.isEmpty()) return ResponseEntity.notFound().build();
        else {
            response = this.interventionService.createAdvancedIntervention(interventionForm, agent.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
