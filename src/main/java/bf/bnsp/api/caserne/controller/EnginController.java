package bf.bnsp.api.caserne.controller;

import bf.bnsp.api.caserne.dto.form.*;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.caserne.service.EnginService;
import bf.bnsp.api.tools.dataType.EEnginType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@RestController
@RequestMapping("/engins")
@CrossOrigin
public class EnginController {

    private final EnginService enginService;

    private final CaserneService caserneService;

    public EnginController(EnginService enginService, CaserneService caserneService) {
        this.enginService = enginService;
        this.caserneService = caserneService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createEngin(@RequestBody EnginCreationForm enginForm){
        Optional<Engin> response;
        if(enginForm.getEnginTypeId() < 1 || enginForm.getEnginTypeId() > EEnginType.values().length) return ResponseEntity.notFound().build();
        else{
            Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(enginForm.getCaserneId());
            if(caserne.isEmpty()) return ResponseEntity.notFound().build();
            else{
                response = this.enginService.createEngin(enginForm, caserne.get());
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateEngin(@RequestBody EnginUpdateForm enginForm){
        Optional<Engin> targetedEngin = this.enginService.findActiveEnginById(enginForm.getEnginId());
        Optional<Engin> response;
        if(targetedEngin.isEmpty() || enginForm.getEnginTypeId() < 1 || enginForm.getEnginTypeId() > EEnginType.values().length) return ResponseEntity.notFound().build();
        else{
            Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(enginForm.getCaserneId());
            if(caserne.isEmpty()) return ResponseEntity.notFound().build();
            else{
                response = this.enginService.updateEngin(enginForm, caserne.get(), targetedEngin.get());
                return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
            }
        }
    }

    @PutMapping(value = "/update/available")
    public ResponseEntity<?> updateEnginAvailable(@RequestBody EnginUpdateAvailability enginForm){
        Optional<Engin> targetedEngin = this.enginService.findActiveEnginById(enginForm.getEnginId());
        if(targetedEngin.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<Engin> response = this.enginService.updateEnginAvailability(targetedEngin.get(), enginForm.isAvailable());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/update/sortie")
    public ResponseEntity<?> updateEnginOut(@RequestBody EnginUpdateOut enginForm){
        Optional<Engin> targetedEngin = this.enginService.findActiveEnginById(enginForm.getEnginId());
        if(targetedEngin.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<Engin> response = this.enginService.updateEnginOut(targetedEngin.get(), enginForm.isOut());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getActiveEnginById(@PathVariable("id") int id){
        Optional<Engin> response = this.enginService.findActiveEnginById(id);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getAllActiveEngin(){
        List<Engin> response = this.enginService.findAllActiveEngin();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/caserne/{caserneId}")
    public ResponseEntity<?> getActiveEnginByCaserne(@PathVariable("caserneId") int caserneId){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(caserneId);
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<Engin> response = this.enginService.findAllActiveEnginByCaserne(caserne.get());
            return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> getActiveEnginByImmatriculation(@RequestBody EnginSearchForm searchForm){
        Optional<Engin> response = this.enginService.findActiveEnginByImmatriculation(searchForm.getImmatriculation());
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/types")
    public ResponseEntity<?> getEnginType(){
        return new ResponseEntity<>(this.enginService.findAllEnginType(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteEnginById(@PathVariable("id") int id){
        Optional<Engin> targetedEngin = this.enginService.findActiveEnginById(id);
        if(targetedEngin.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<Engin> response = this.enginService.deleteEngin(targetedEngin.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
