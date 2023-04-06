package bf.bnsp.api.account.controller;

import bf.bnsp.api.account.dto.form.DailyProgramCreationForm;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.service.DailyProgramService;
import bf.bnsp.api.account.service.EquipeService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/programs")
@CrossOrigin
public class ProgramController {

    private final DailyProgramService dailyProgramService;

    private final CaserneService caserneService;

    private final EquipeService equipeService;

    public ProgramController(DailyProgramService dailyProgramService, CaserneService caserneService, EquipeService equipeService) {
        this.dailyProgramService = dailyProgramService;
        this.caserneService = caserneService;
        this.equipeService = equipeService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createDailyProgram(@RequestBody DailyProgramCreationForm dailyProgramForm){
        Optional<DailyProgram> response;
        Optional<Equipe> team = this.equipeService.findActiveEquipeById(dailyProgramForm.getTeamProgramList().get(0).getEquipeId());
        if(team.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.dailyProgramService.createDailyProgram(dailyProgramForm, team.get().getCaserne());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = {"/", ""})
    public final ResponseEntity<?> getAllDailyPrograms(){
        List<DailyProgram> response = this.dailyProgramService.findAllDailyProgram();
        return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/caserne/{caserneId}")
    public final ResponseEntity<?> getDailyProgramsByCaserne(@PathVariable("caserneId") int caserneId, @RequestParam("date") @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<LocalDate> date){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(caserneId);
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            if(date.isEmpty()){
                List<DailyProgram> response = this.dailyProgramService.findAllDailyProgramByCaserne(caserne.get());
                return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
            }
            else {
                Optional<DailyProgram> response = this.dailyProgramService.findDailyProgramByCaserneAndDate(caserne.get(), date.get());
                return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
            }
        }
    }
}
