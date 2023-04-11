package bf.bnsp.api.account.controller;

import bf.bnsp.api.account.dto.form.DailyProgramCreationForm;
import bf.bnsp.api.account.dto.form.DailyProgramKeysForm;
import bf.bnsp.api.account.model.Fonction;
import bf.bnsp.api.account.service.FonctionService;
import bf.bnsp.api.account.service.EquipeService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.tools.mappingTools.MappingTool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/programs")
@CrossOrigin
public class ProgramController {

    private final FonctionService dailyProgramService;

    private final CaserneService caserneService;

    private final EquipeService equipeService;

    private final MappingTool mappingTool;

    public ProgramController(FonctionService dailyProgramService, CaserneService caserneService, EquipeService equipeService, MappingTool mappingTool) {
        this.dailyProgramService = dailyProgramService;
        this.caserneService = caserneService;
        this.equipeService = equipeService;
        this.mappingTool = mappingTool;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createDailyProgram(@RequestBody DailyProgramCreationForm dailyProgramForm){
        List<Fonction> response = this.dailyProgramService.createDailyProgram(dailyProgramForm);
        return response.size() > 0 ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response), HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = {"/", ""})
    public final ResponseEntity<?> getAllDailyPrograms(){
        List<Fonction> response = this.dailyProgramService.findAllDailyProgram();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/caserne")
    public final ResponseEntity<?> getDailyProgramsByCaserne(@RequestBody DailyProgramKeysForm keysForm){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(keysForm.getCaserneId());
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<Fonction> response = this.dailyProgramService.findDailyProgramDetailByCaserneAndDate(caserne.get(), keysForm.getDate());
            return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    /*@GetMapping(value = "/search")
    public final ResponseEntity<?> tmpFunction(@RequestBody DailyProgramKeysForm keysForm){
        Optional<Equipe> equipe = this.equipeService.findActiveEquipeById(keysForm.getEquipeId());
        if(equipe.isEmpty()) return ResponseEntity.notFound().build();
        else{
            DailyProgramKeys keys = new DailyProgramKeys(keysForm.getDate(), equipe.get());
            Optional<DailyProgram> dailyProgram = this.dailyProgramService.findDailyProgramByKey(keys);
            if(dailyProgram.isEmpty()) return ResponseEntity.notFound().build();
            else{
                List<Fonction> response = this.dailyProgramService.findAllFonctionByDailyProgram(dailyProgram.get());
                return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
            }

        }
    }*/
}
