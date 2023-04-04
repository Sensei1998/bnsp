package bf.bnsp.api.caserne.controller;

import bf.bnsp.api.caserne.dto.form.CaserneCreationForm;
import bf.bnsp.api.caserne.dto.form.CaserneUpdateForm;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/casernes")
@CrossOrigin
public class CaserneController {

    private final CaserneService caserneService;

    public CaserneController(CaserneService caserneService) {
        this.caserneService = caserneService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createBrigade(@RequestBody CaserneCreationForm caserneForm){
        Optional<Caserne> response;
        if(caserneForm.getIdCaserneType() < 1 || caserneForm.getIdCaserneType() > 3) return ResponseEntity.notFound().build();
        else if (caserneForm.getIdCaserneParent() > 0){
            Optional<Caserne> caserneParent = this.caserneService.findActiveCaserneById(caserneForm.getIdCaserneParent());
            if(caserneParent.isEmpty()) return ResponseEntity.notFound().build();
            else {
                response = this.caserneService.createCaserne(caserneForm, caserneParent);
                return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            response = this.caserneService.createCaserne(caserneForm, Optional.empty());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateBrigade(@RequestBody CaserneUpdateForm caserneForm){
        Optional<Caserne> targetBrigade = this.caserneService.findActiveCaserneById(caserneForm.getId());
        Optional<Caserne> response;
        if(targetBrigade.isEmpty() || caserneForm.getIdCaserneType() < 1 || caserneForm.getIdCaserneType() > 3) return ResponseEntity.notFound().build();
        else if (caserneForm.getIdCaserneParent() > 0){
            Optional<Caserne> caserneParent = this.caserneService.findActiveCaserneById(caserneForm.getIdCaserneParent());
            if(caserneParent.isEmpty()) return ResponseEntity.notFound().build();
            else {
                response = this.caserneService.updateCaserne(caserneForm, caserneParent, targetBrigade.get());
                return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else {
            response = this.caserneService.updateCaserne(caserneForm, Optional.empty(), targetBrigade.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.ACCEPTED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getActiveBrigadeById(@PathVariable("id") int id){
        Optional<Caserne> response = this.caserneService.findActiveCaserneById(id);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> getActiveBrigades(){
        List<Caserne> response = this.caserneService.findAllActiveCaserne();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/types")
    public ResponseEntity<?> getCaserneType(){
        return new ResponseEntity<>(this.caserneService.findAllCaserneType(), HttpStatus.OK);
    }

    @GetMapping(value = "/types/{id}")
    public ResponseEntity<?> getActiveCaserneByCategoryType(@PathVariable("id") int categoryId){
        if(categoryId < 1 || categoryId > 3) return ResponseEntity.notFound().build();
        else{
            List<Caserne> response = this.caserneService.findByCaserneTypeAndHiddenFalse(categoryId);
            return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/affiliation/{id}")
    public ResponseEntity<?> getActiveCaserneByAffiliation(@PathVariable("id") int affiliation){
        Optional<Caserne> targetBrigade = this.caserneService.findActiveCaserneById(affiliation);
        if(targetBrigade.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<Caserne> response = this.caserneService.findActiveCaserneByAffiliation(targetBrigade.get());
            return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteBrigadeById(@PathVariable("id") int id){
        Optional<Caserne> targetBrigade = this.caserneService.findActiveCaserneById(id);
        if(targetBrigade.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<Caserne> response = this.caserneService.deleteCaserne(targetBrigade.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
