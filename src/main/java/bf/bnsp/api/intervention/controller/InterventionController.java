package bf.bnsp.api.intervention.controller;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.service.AgentService;
import bf.bnsp.api.intervention.dto.form.InterventionInitAdvancedForm;
import bf.bnsp.api.intervention.dto.form.InterventionInitForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateGeneralForm;
import bf.bnsp.api.intervention.dto.form.InterventionUpdateLocationForm;
import bf.bnsp.api.intervention.dto.response.InterventionResponse;
import bf.bnsp.api.intervention.model.CategoryIncident;
import bf.bnsp.api.intervention.model.IncidentType;
import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.service.InterventionService;
import bf.bnsp.api.intervention.service.InterventionSheetService;
import bf.bnsp.api.tools.controleForm.TokenUtils;
import bf.bnsp.api.tools.mappingTools.MappingIntervention;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final InterventionSheetService interventionSheetService;

    private final MappingIntervention mappingIntervention;

    private final TokenUtils tokenUtils;

    public InterventionController(AgentService agentService, InterventionService interventionService, InterventionSheetService interventionSheetService, MappingIntervention mappingIntervention, TokenUtils tokenUtils) {
        this.agentService = agentService;
        this.interventionService = interventionService;
        this.interventionSheetService = interventionSheetService;
        this.mappingIntervention = mappingIntervention;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping(value = "/create/partial")
    public ResponseEntity<?> createPartialIntervention(@RequestBody InterventionInitForm interventionForm, @RequestHeader("Authorization") String token){
        Optional<Intervention> response;
        Optional<Agent> agent = this.tokenUtils.getAgentFromToken(token);
        if(agent.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else {
            response = this.interventionService.createBasicIntervention(interventionForm, agent.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/create/complete")
    public ResponseEntity<?> createCompleteIntervention(@RequestBody InterventionInitAdvancedForm interventionForm, @RequestHeader("Authorization") String token){
        Optional<Intervention> response;
        Optional<Agent> agent = this.tokenUtils.getAgentFromToken(token);
        if(agent.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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

    @PutMapping(value = "/update/close/{interventionId}")
    public ResponseEntity<?> closeIntervention(@PathVariable("interventionId") int id){
        Optional<Intervention> response = this.interventionService.findActiveInterventionById(id);
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.interventionService.closeIntervention(response.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/count/status")
    public ResponseEntity<?> countByStatus(@RequestParam("current") Optional<Boolean> currentDate){
        Map<String, Long> response = this.interventionService.countInterventionByStatus(currentDate.isPresent());
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/count/interval")
    public ResponseEntity<?> countByInterval(@RequestParam("start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate, @RequestParam("end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate){
        Map<String, Long> response = this.interventionService.countInterventionByInterval(startDate, endDate);
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/count/date")
    public ResponseEntity<?> countByInterval(@RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date){
        long response = this.interventionService.countAllInterventionByDate(date);
        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping(value = "")
    public ResponseEntity<?> getIncident(@RequestParam("id") Optional<Integer> interventionId){
        if(interventionId.isEmpty()){
            List<Intervention> intervention = this.interventionService.findAllActiveIntervention();
            List<Optional<InterventionResponse>> responses = new ArrayList<>();
            for(Intervention element : intervention){
                responses.add(this.mappingIntervention.mappingIntervention(this.interventionSheetService.findActiveInterventionSheetByIntervention(element), Optional.of(element)));
            }
            return responses.size() > 0 ? new ResponseEntity<>(responses, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
        else{
            Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(interventionId.get());
            if(intervention.isEmpty()) return ResponseEntity.notFound().build();
            List<InterventionSheet> response = this.interventionSheetService.findActiveInterventionSheetByIntervention(intervention.get());
            return response.size() > 0 ? new ResponseEntity<>(this.mappingIntervention.mappingIntervention(response, intervention), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

}
