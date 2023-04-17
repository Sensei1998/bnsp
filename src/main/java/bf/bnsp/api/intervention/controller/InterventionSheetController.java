package bf.bnsp.api.intervention.controller;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.intervention.dto.form.InterventionSheetConfigOutForm;
import bf.bnsp.api.intervention.dto.form.InterventionSheetMessageForm;
import bf.bnsp.api.intervention.model.Intervention;
import bf.bnsp.api.intervention.model.InterventionSheet;
import bf.bnsp.api.intervention.model.InterventionSheetToMessage;
import bf.bnsp.api.intervention.model.InterventionSheetToTeam;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import bf.bnsp.api.intervention.service.InterventionService;
import bf.bnsp.api.intervention.service.InterventionSheetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/intervention/sheet")
@CrossOrigin
public class InterventionSheetController {

    private final InterventionSheetService interventionSheetService;

    private final InterventionService interventionService;

    private final CaserneService caserneService;


    public InterventionSheetController(InterventionSheetService interventionSheetService, InterventionService interventionService, CaserneService caserneService) {
        this.interventionSheetService = interventionSheetService;
        this.interventionService = interventionService;
        this.caserneService = caserneService;
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateInterventionSheet(@RequestBody InterventionSheetConfigOutForm interventionForm){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(interventionForm.getCaserneId());
        Optional<Intervention> intervention = this.interventionService.findActiveInterventionById(interventionForm.getInterventionId());
        if(caserne.isEmpty() || intervention.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<InterventionSheet> interventionSheet = this.interventionSheetService.findActiveInterventionSheetById(new InterventionFollowedKey(intervention.get(), caserne.get()));
            if(interventionSheet.isEmpty()) return ResponseEntity.notFound().build();
            else{
                List<InterventionSheetToTeam> response = this.interventionSheetService.updateInterventionSheet(interventionForm, interventionSheet.get());
                return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
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
                return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }


}
