package bf.bnsp.api.caserne.controller;

import bf.bnsp.api.caserne.dto.form.AffiliationCreateForm;
import bf.bnsp.api.caserne.dto.form.AffiliationListForm;
import bf.bnsp.api.caserne.dto.form.CaserneCreationForm;
import bf.bnsp.api.caserne.dto.form.CaserneUpdateForm;
import bf.bnsp.api.caserne.dto.response.CaserneResponse;
import bf.bnsp.api.caserne.model.*;
import bf.bnsp.api.caserne.service.CaserneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
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
        else if (caserneForm.getIdCaserneParent().isPresent() && caserneForm.getIdCaserneParent().get() > 0){
            Optional<Caserne> caserneParent = this.caserneService.findActiveCaserneById(caserneForm.getIdCaserneParent().get());
            if(caserneParent.isEmpty()) return ResponseEntity.notFound().build();
            else {
                response = this.caserneService.createCaserne(caserneForm, caserneParent);
                this.caserneService.setAffiliationLink(caserneForm.getIdAffiliation(), caserneParent.get(), response.get());
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
        else if (caserneForm.getIdCaserneParent().isPresent() && caserneForm.getIdCaserneParent().get() > 0){
            Optional<Caserne> caserneParent = this.caserneService.findActiveCaserneById(caserneForm.getIdCaserneParent().get());
            if(caserneParent.isEmpty()) return ResponseEntity.notFound().build();
            else {
                response = this.caserneService.updateCaserne(caserneForm, caserneParent, targetBrigade.get());
                this.caserneService.setAffiliationLink(caserneForm.getIdAffiliation(), caserneParent.get(), response.get());
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
        Optional<CaserneResponse> response = this.caserneService.findActiveCaserneResponseById(id);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/parent")
    public ResponseEntity<?> getParentAffiliation(@PathVariable("id") int id, @RequestParam("affiliation") Optional<Integer> affiliationId){
        Optional<Caserne> response = this.caserneService.findActiveCaserneById(id);
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        response = this.caserneService.findParentAffiliationCaserne(affiliationId, response.get());
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}/children")
    public ResponseEntity<?> getChildrenAffiliation(@PathVariable("id") int id, @RequestParam("affiliation") Optional<Integer> affiliationId){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(id);
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        List<Caserne> response = this.caserneService.findChildrenAffiliationCaserne(affiliationId, caserne.get());
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteBrigadeById(@PathVariable("id") int id){
        Optional<Caserne> targetBrigade = this.caserneService.findActiveCaserneById(id);
        if(targetBrigade.isEmpty()) return ResponseEntity.notFound().build();
        else {
            Optional<Caserne> response = this.caserneService.deleteCaserne(targetBrigade.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/affiliation/create")
    public ResponseEntity<?> createAffiliation(@RequestBody AffiliationCreateForm affiliationCreateForm){
        Optional<Affiliation> response = this.caserneService.createAffiliation(affiliationCreateForm);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping(value = "/affiliation/{id}/default")
    public ResponseEntity<?> updateDefaultAffiliation(@PathVariable("id") int id){
        Optional<Affiliation> response = this.caserneService.findActiveAffiliationById(id);
        if(response.isPresent()) response = this.caserneService.setActiveAffiliationDefault(response.get());
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = {"/affiliation/", "/affiliation"})
    public ResponseEntity<?> getAllActiveAffiliation(){
        List<Affiliation> response = this.caserneService.findAllActiveAffiliation();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/affiliation/default")
    public ResponseEntity<?> getDefaultAffiliation(){
        Optional<Affiliation> response = this.caserneService.findDefaultAffiliation();
        return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/affiliation/{id}")
    public ResponseEntity<?> getAffiliationById(@PathVariable("id") int id){
        Optional<Affiliation> response = this.caserneService.findActiveAffiliationById(id);
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/affiliation/{id}")
    public ResponseEntity<?> deleteAffiliationById(@PathVariable("id") int id){
        Optional<Affiliation> response = this.caserneService.findActiveAffiliationById(id);
        if(response.isPresent()) response = this.caserneService.deleteAffiliation(response.get());
        return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/affiliation//link/create")
    public ResponseEntity<?> setAffiliationLink(@RequestBody AffiliationListForm listForm){
        List<AffiliationLink> response = this.caserneService.setAffiliationListLink(listForm);
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping(value = "/affiliation/link/{linkId}")
    public ResponseEntity<?> getAffiliationLinkById(@PathVariable("linkId") int id){
        Optional<AffiliationLink> response = this.caserneService.findAffiliationLinkById(id);
        return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/affiliation//link/{linkId}")
    public ResponseEntity<?> deleteAffiliationLinkById(@PathVariable("linkId") int id){
        Optional<AffiliationLink> response = this.caserneService.findAffiliationLinkById(id);
        if (response.isPresent()){
            response = this.caserneService.deleteAffiliationLink(response.get());
            return new ResponseEntity<>(response.get(), HttpStatus.ACCEPTED);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/regions")
    public ResponseEntity<?> getAllZonesByRegion(){
        List<Region> response = this.caserneService.findAllRegions();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/zones")
    public ResponseEntity<?> getAllZones(){
        List<Zone> response = this.caserneService.findAllZones();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
