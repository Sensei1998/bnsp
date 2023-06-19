package bf.bnsp.api.intervention.controller;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.service.DailyProgramService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.intervention.dto.form.*;
import bf.bnsp.api.intervention.dto.response.InterventionResponse;
import bf.bnsp.api.intervention.model.*;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import bf.bnsp.api.intervention.service.InterventionService;
import bf.bnsp.api.intervention.service.InterventionSheetService;
import bf.bnsp.api.tools.controleForm.TokenUtils;
import bf.bnsp.api.tools.mappingTools.MappingIntervention;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@RestController
@RequestMapping("/intervention/sheet")
@CrossOrigin
public class InterventionSheetController {

    private final InterventionSheetService interventionSheetService;

    private final InterventionService interventionService;

    private final CaserneService caserneService;

    private final TokenUtils tokenUtils;

    private final DailyProgramService dailyProgramService;

    private final MappingIntervention mappingIntervention;


    public InterventionSheetController(InterventionSheetService interventionSheetService, InterventionService interventionService, CaserneService caserneService, TokenUtils tokenUtils, DailyProgramService dailyProgramService, MappingIntervention mappingIntervention) {
        this.interventionSheetService = interventionSheetService;
        this.interventionService = interventionService;
        this.caserneService = caserneService;
        this.tokenUtils = tokenUtils;
        this.dailyProgramService = dailyProgramService;
        this.mappingIntervention = mappingIntervention;
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateInterventionSheet(@RequestBody InterventionSheetConfigOutForm interventionForm, @RequestHeader("Authorization") String token){
        Optional<Agent> agent = this.tokenUtils.getAgentFromToken(token);
        if(agent.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(interventionForm.getCaserneId());
        Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(interventionForm.getInterventionId());
        if(caserne.isEmpty() || intervention.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<InterventionSheet> interventionSheet = this.interventionSheetService.findActiveInterventionSheetById(new InterventionFollowedKey(intervention.get(), caserne.get()));
            if(interventionSheet.isEmpty()) return ResponseEntity.notFound().build();
            else{
                List<InterventionSheetToTeam> response = this.interventionSheetService.updateInterventionSheet(interventionForm, interventionSheet.get(), agent.get());
                return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @PutMapping(value = "/update/team")
    public ResponseEntity<?> updateInterventionSheetTeam(@RequestBody InterventionTeamUpdate interventionForm){
        Optional<InterventionSheetToTeam> team = this.interventionSheetService.findInterventionTeamById(interventionForm.getInterventionTeamId());
        if(team.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<InterventionSheetToTeam> response = this.interventionSheetService.updateInterventionTeam(interventionForm, team.get());
            return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/message")
    public ResponseEntity<?> createInterventionSheetMessage(@RequestBody InterventionSheetMessageForm messageFormForm){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(messageFormForm.getCaserneId());
        Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(messageFormForm.getInterventionId());
        if(caserne.isEmpty() || intervention.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<InterventionSheet> interventionSheet = this.interventionSheetService.findActiveInterventionSheetById(new InterventionFollowedKey(intervention.get(), caserne.get()));
            if(interventionSheet.isEmpty()) return ResponseEntity.notFound().build();
            else{
                Optional<InterventionSheetToMessage> response = this.interventionSheetService.createInterventionMessage(messageFormForm, interventionSheet.get());
                return response.isPresent() ? new ResponseEntity<>(this.mappingIntervention.mappingMessage(Arrays.asList(response.get())).get(0), HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @DeleteMapping(value = "/delete/team/{id}")
    public ResponseEntity<?> deleteInterventionSheetTeam(@PathVariable("id") int teamId){
        Optional<InterventionSheetToTeam> team = this.interventionSheetService.findInterventionTeamById(teamId);
        if(team.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<InterventionSheetToTeam> response = this.interventionSheetService.deleteInterventionTeam(team.get());
            return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/caserne/{caserneId}")
    public ResponseEntity<?> findInterventionSheetByCaserne(@PathVariable("caserneId") int caserneId){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(caserneId);
        List<Optional<InterventionResponse>> responses = new ArrayList<>();
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<InterventionSheet> tmpSheets = this.interventionSheetService.findActiveInterventionSheetByCaserne(caserne.get());
            for (InterventionSheet element: tmpSheets) {
                responses.add(this.mappingIntervention.mappingIntervention(this.interventionSheetService.findActiveInterventionSheetByIntervention(element.getKey().getIntervention()), Optional.of(element.getKey().getIntervention())));
            }
            return responses.size() > 0 ? new ResponseEntity<>(responses, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/message/{messageId}")
    public ResponseEntity<?> getMessageDetailById(@PathVariable("messageId") int messageId){
        Optional<InterventionSheetToMessage> response = this.interventionSheetService.findMessageById(messageId);
        return response.isPresent() ? new ResponseEntity<>(this.mappingIntervention.mappingMessage(Arrays.asList(response.get())).get(0), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/message/intervention/{interventionId}/caserne/{caserneId}")
    public ResponseEntity<?> getMessageByInterventionOrFilter(@PathVariable("interventionId") int interventionId, @PathVariable("caserneId") int caserneId, @RequestParam("equipe") Optional<Integer> equipeId){
        Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(interventionId);
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(caserneId);
        List<InterventionSheetToMessage> response = new ArrayList<>();
        if(intervention.isEmpty() || caserne.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<InterventionSheet> interventionSheet = this.interventionSheetService.findActiveInterventionSheetById(new InterventionFollowedKey(intervention.get(), caserne.get()));
            if(interventionSheet.isEmpty()) return ResponseEntity.notFound().build();
            if(equipeId.isEmpty()){
                response = this.interventionSheetService.findMessagesByInterventionSheet(interventionSheet.get());
                return response.size() > 0 ? new ResponseEntity<>(this.mappingIntervention.mappingMessage(response), HttpStatus.OK) : ResponseEntity.noContent().build();
            }
            else {
                Optional<DailyTeam> equipe =  this.dailyProgramService.findActiveDailyTeamById(equipeId.get());
                if(equipe.isEmpty()) return ResponseEntity.notFound().build();
                else{
                    response = this.interventionSheetService.findMessagesByInterventionSheetAndTeam(interventionSheet.get(), equipe.get());
                    return response.size() > 0 ? new ResponseEntity<>(this.mappingIntervention.mappingMessage(response), HttpStatus.OK) : ResponseEntity.noContent().build();
                }
            }
        }
    }

    @DeleteMapping(value = "/message/{messageId}")
    public ResponseEntity<?> deleteMessageDetailById(@PathVariable("messageId") int messageId){
        Optional<InterventionSheetToMessage> response = this.interventionSheetService.deleteMessageById(messageId);
        return response.isPresent() ? new ResponseEntity<>(this.mappingIntervention.mappingMessage(Arrays.asList(response.get())).get(0), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/incident")
    public ResponseEntity<?> createIncidentSheet(@RequestBody IncidentInformationForm incidentForm, @RequestHeader("Authorization") String token){
        Optional<Agent> agent = this.tokenUtils.getAgentFromToken(token);
        Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(incidentForm.getInterventionId());
        Optional<Caserne> caserne = Optional.empty();
        if(incidentForm.getCaserneId().isEmpty()) caserne = this.tokenUtils.getCaserneFromToken(token);
        else caserne = this.caserneService.findActiveCaserneById(incidentForm.getCaserneId().get());

        if(agent.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if(caserne.isEmpty() || intervention.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<InterventionSheet> interventionSheet = this.interventionSheetService.findActiveInterventionSheetById(new InterventionFollowedKey(intervention.get(), caserne.get()));;
            if(interventionSheet.isEmpty()) return ResponseEntity.notFound().build();
            else{
                Optional<Sinister> response = this.interventionSheetService.createSinisterSheet(incidentForm, interventionSheet.get(), agent.get());
                return response.isPresent() ? new ResponseEntity<>(this.mappingIntervention.mappingSinisterSheetOneCaserne(Arrays.asList(response.get())), HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @PutMapping(value = "/incident/update")
    public ResponseEntity<?> updateIncidentSheet(@RequestBody IncidentInformationUpdateForm incidentForm, @RequestHeader("Authorization") String token){
        Optional<Sinister> targetedIncident = this.interventionSheetService.findActiveSinisterById(incidentForm.getIncidentId());
        Optional<Agent> agent = this.tokenUtils.getAgentFromToken(token);
        Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(incidentForm.getInterventionId());
        Optional<Caserne> caserne = Optional.empty();
        if(incidentForm.getCaserneId().isEmpty()) caserne = this.tokenUtils.getCaserneFromToken(token);
        else caserne = this.caserneService.findActiveCaserneById(incidentForm.getCaserneId().get());

        if(agent.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else if(caserne.isEmpty() || intervention.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<InterventionSheet> interventionSheet = this.interventionSheetService.findActiveInterventionSheetById(new InterventionFollowedKey(intervention.get(), caserne.get()));;
            if(interventionSheet.isEmpty()) return ResponseEntity.notFound().build();
            else{
                Optional<Sinister> response = this.interventionSheetService.updateSinisterSheet(incidentForm, interventionSheet.get(), agent.get(), targetedIncident.get());
                return response.isPresent() ? new ResponseEntity<>(this.mappingIntervention.mappingSinisterSheetOneCaserne(Arrays.asList(response.get())), HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @GetMapping(value = "/incident/{incidentId}")
    public ResponseEntity<?> getIncidentDetailById(@PathVariable("incidentId") int incidentId){
        Optional<Sinister> response = this.interventionSheetService.findActiveSinisterById(incidentId);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/incident/intervention/{interventionId}")
    public ResponseEntity<?> getIncidentByInterventionOrFilter(@PathVariable("interventionId") int interventionId, @RequestParam("caserne") Optional<Integer> caserneId){
        Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(interventionId);
        List<Sinister> response = new ArrayList<>();
        if(intervention.isEmpty()) return ResponseEntity.notFound().build();
        else {
            if(caserneId.isEmpty()){
                response = this.interventionSheetService.findAllActiveSinisterByIntervention(intervention.get());
                return response.size() > 0 ? new ResponseEntity<>(this.mappingIntervention.mappingSinisterSheetMultiCaserne(response), HttpStatus.OK) : ResponseEntity.noContent().build();
            }
            else {
                Optional<Caserne> caserne =  this.caserneService.findActiveCaserneById(caserneId.get());
                if(caserne.isEmpty()) return ResponseEntity.notFound().build();
                else{
                    Optional<InterventionSheet> interventionSheet = this.interventionSheetService.findActiveInterventionSheetById(new InterventionFollowedKey(intervention.get(), caserne.get()));
                    if(interventionSheet.isEmpty()) return ResponseEntity.notFound().build();
                    else {
                        response = this.interventionSheetService.findAllActiveSinisterByInterventionSheet(interventionSheet.get());
                        return response.size() > 0 ? new ResponseEntity<>(this.mappingIntervention.mappingSinisterSheetOneCaserne(response), HttpStatus.OK) : ResponseEntity.noContent().build();
                    }
                }
            }
        }
    }

    @DeleteMapping(value = "/incident/{incidentId}")
    public ResponseEntity<?> removeIncidentDetailById(@PathVariable("incidentId") int incidentId){
        Optional<Sinister> response = this.interventionSheetService.deleteActiveSinisterById(incidentId);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

}
