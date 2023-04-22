package bf.bnsp.api.account.controller;

import bf.bnsp.api.account.dto.form.*;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.service.DailyProgramService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.tools.controleForm.TokenUtils;
import bf.bnsp.api.tools.mappingTools.MappingTool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/programs")
@CrossOrigin
public class ProgramController {

    private final DailyProgramService dailyProgramService;

    private final CaserneService caserneService;

    private final TokenUtils tokenUtils;

    private final MappingTool mappingTool;

    public ProgramController(DailyProgramService dailyProgramServ, CaserneService caserneService, TokenUtils tokenUtils, MappingTool mappingTool) {
        this.dailyProgramService = dailyProgramServ;
        this.caserneService = caserneService;
        this.tokenUtils = tokenUtils;
        this.mappingTool = mappingTool;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createDailyProgram(@RequestBody DailyProgramInitForm dailyProgramForm, @RequestHeader("Authorization") String token){
        Optional<Caserne> caserne = Optional.empty();
        if(dailyProgramForm.getCaserneId().isEmpty()) caserne = this.tokenUtils.getCaserneFromToken(token);
        else caserne = this.caserneService.findActiveCaserneById(dailyProgramForm.getCaserneId().get());
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();

        Optional<DailyProgram> response = this.dailyProgramService.createDailyProgram(dailyProgramForm, caserne.get());
        return response.isPresent() ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response.get()), HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> getDailyProgramByDateAndCaserne(@RequestBody DailyProgramSearchForm searchForm, @RequestHeader("Authorization") String token){
        Optional<Caserne> caserne = Optional.empty();
        if(searchForm.getCaserneId().isEmpty()) caserne = this.tokenUtils.getCaserneFromToken(token);
        else caserne = this.caserneService.findActiveCaserneById(searchForm.getCaserneId().get());

        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<DailyProgram> response = this.dailyProgramService.findActiveDailyProgramByDateAndCaserne(searchForm.getDate(), caserne.get());
            return response.isPresent() ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response.get()), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "/team/add")
    public ResponseEntity<?> addTeamToDailyProgram(@RequestBody DailyTeamAddForm dailyProgramForm){
        Optional<DailyProgram> response = this.dailyProgramService.findActiveDailyProgramById(dailyProgramForm.getProgramId());
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.dailyProgramService.addTeamToDailyProgram(dailyProgramForm, response.get());
            return response.isPresent() ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response.get()), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "/team/update")
    public ResponseEntity<?> updateDailyTeam(@RequestBody DailyTeamUpdateForm teamForm){
        Optional<DailyTeam> response = this.dailyProgramService.findActiveDailyTeamById(teamForm.getTeamId());
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.dailyProgramService.updateTeamComposition(teamForm, response.get());
            return response.isPresent() ? new ResponseEntity<>(this.mappingTool.mappingDailyTeam(response.get()), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/team/{teamId}")
    public final ResponseEntity<?> getDailyTeamById(@PathVariable("teamId") long id){
        Optional<DailyTeam> response = this.dailyProgramService.findActiveDailyTeamById(id);
        return response.isPresent() ? new ResponseEntity<>(this.mappingTool.mappingDailyTeam(response.get()), HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/team/search")
    public ResponseEntity<?> getDailyTeamByDateAndCaserne(@RequestBody DailyProgramSearchForm searchForm, @RequestHeader("Authorization") String token){
        Optional<Caserne> caserne = Optional.empty();
        if(searchForm.getCaserneId().isEmpty()) caserne = this.tokenUtils.getCaserneFromToken(token);
        else caserne = this.caserneService.findActiveCaserneById(searchForm.getCaserneId().get());

        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<DailyProgram> response = this.dailyProgramService.findActiveDailyProgramByDateAndCaserne(searchForm.getDate(), caserne.get());
            return response.isPresent() ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response.get()).getTeams(), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(value = "/team/{teamId}")
    public final ResponseEntity<?> deleteDailyTeamById(@PathVariable("teamId") long id){
        Optional<DailyTeam> response = this.dailyProgramService.findActiveDailyTeamById(id);
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else {
            response = this.dailyProgramService.deleteDailyTeam(response.get());
            return response.isPresent() ? new ResponseEntity<>(this.mappingTool.mappingDailyTeam(response.get()), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

}
