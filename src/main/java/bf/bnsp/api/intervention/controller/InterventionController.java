package bf.bnsp.api.intervention.controller;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.service.AgentService;
import bf.bnsp.api.intervention.dto.form.InterventionInitAdvancedForm;
import bf.bnsp.api.intervention.dto.form.InterventionInitForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateGeneralForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateLocationForm;
import bf.bnsp.api.intervention.model.CategoryIncident;
import bf.bnsp.api.intervention.model.IncidentType;
import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.service.InterventionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
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

    @PutMapping(value = "/update/location")
    public ResponseEntity<?> updateInterventionLocation(@RequestBody InterventionUpdateLocationForm locationForm){
        Optional<Intervention> response = this.interventionService.findActiveInterventionById(locationForm.getId());
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.interventionService.updateInterventionLocation(locationForm, response.get().getAgentCCOT(), response.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/update/info")
    public ResponseEntity<?> updateInterventionInfo(@RequestBody InterventionUpdateGeneralForm infoForm){
        Optional<Intervention> response = this.interventionService.findActiveInterventionById(infoForm.getId());
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.interventionService.updateInterventionGeneralInfo(infoForm, response.get().getAgentCCOT(), response.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/types/category")
    public ResponseEntity<?> getCategoriesIncident(){
        List<CategoryIncident> response = this.interventionService.findAllCategoryIncident();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/types")
    public ResponseEntity<?> getIncidentType(@RequestParam("category") Optional<Integer> categoryId){
        List<IncidentType> response = categoryId.isEmpty() ? this.interventionService.findAllIncidentType() : this.interventionService.findAllIncidentTypeByCategoryIncidentId(categoryId.get());
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/types/{typeId}")
    public ResponseEntity<?> getIncidentType(@PathVariable("typeId") int typeId){
        Optional<IncidentType> response = this.interventionService.findIncidentTypeById(typeId);
        return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.notFound().build();
    }

}
