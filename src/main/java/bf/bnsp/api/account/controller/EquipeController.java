package bf.bnsp.api.account.controller;

import bf.bnsp.api.account.dto.form.EquipeCreationForm;
import bf.bnsp.api.account.dto.form.EquipeUpdateForm;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.service.EquipeService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.caserne.service.EnginService;
import bf.bnsp.api.tools.dataType.EEquipeType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
@CrossOrigin
public class EquipeController {

    private final EquipeService equipeService;

    private final CaserneService caserneService;

    private final EnginService enginService;

    public EquipeController(EquipeService equipeService, CaserneService caserneService, EnginService enginService) {
        this.equipeService = equipeService;
        this.caserneService = caserneService;
        this.enginService = enginService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createTeam(@RequestBody EquipeCreationForm equipeForm){
        Optional<Equipe> response;
        if(equipeForm.getEquipeTypeId() < 1 || equipeForm.getEquipeTypeId() > EEquipeType.values().length) return ResponseEntity.notFound().build();
        else{
            Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(equipeForm.getCaserneId());
            if(caserne.isEmpty()) return ResponseEntity.notFound().build();
            else{
                Optional<Engin> engin = this.enginService.findActiveEnginByCaserneAndId(equipeForm.getEnginId(), caserne.get());
                if(equipeForm.getEnginId() != -1 && engin.isEmpty()) return ResponseEntity.notFound().build();
                response = this.equipeService.createEquipe(equipeForm, caserne.get(), engin);
                return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : ResponseEntity.notFound().build();
            }
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateTeam(@RequestBody EquipeUpdateForm equipeForm){
        Optional<Equipe> targetedEquipe = this.equipeService.findActiveEquipeById(equipeForm.getEquipeId());
        Optional<Equipe> response;
        if(targetedEquipe.isEmpty() || equipeForm.getEquipeTypeId() < 1 || equipeForm.getEquipeTypeId() > EEquipeType.values().length) return ResponseEntity.notFound().build();
        else{
            Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(equipeForm.getCaserneId());
            if(caserne.isEmpty()) return ResponseEntity.notFound().build();
            else{
                Optional<Engin> engin = this.enginService.findActiveEnginByCaserneAndId(equipeForm.getEnginId(), caserne.get());
                if(equipeForm.getEnginId() != -1 && engin.isEmpty()) return ResponseEntity.notFound().build();
                response = this.equipeService.updateEquipe(equipeForm, caserne.get(), engin, targetedEquipe.get());
                return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.ACCEPTED) : ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping(value = {"/", ""})
    public final ResponseEntity<?> getAllActiveTeam(){
        List<Equipe> response = this.equipeService.findAllActiveEquipe();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public final ResponseEntity<?> getActiveTeamById(@PathVariable("id") int equipeId){
        Optional<Equipe> response = this.equipeService.findActiveEquipeById(equipeId);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/caserne/{caserneId}")
    public final ResponseEntity<?> getAllActiveTeamByCaserneAndType(@PathVariable("caserneId") int caserneId, @RequestParam("type") Optional<Integer> equipeTypeId){
        List<Equipe> response;
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(caserneId);
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            if(equipeTypeId.isEmpty()) response = this.equipeService.findAllActiveEquipeByCaserne(caserne.get());
            else if(equipeTypeId.get() < 1 || equipeTypeId.get() > EEquipeType.values().length) return ResponseEntity.notFound().build();
            else response = this.equipeService.findAllActiveEquipeByCaserneAndType(caserne.get(), equipeTypeId.get());
            return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/types")
    public final ResponseEntity<?> getAllTypes(){
        return new ResponseEntity<>(this.equipeService.findAllEquipeType(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable("id") int equipeId){
        Optional<Equipe> targetedEquipe = this.equipeService.findActiveEquipeById(equipeId);
        Optional<Equipe> response;
        if(targetedEquipe.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.equipeService.deleteEquipe(targetedEquipe.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
