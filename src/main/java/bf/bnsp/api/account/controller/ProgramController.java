package bf.bnsp.api.account.controller;

import bf.bnsp.api.account.dto.form.*;
import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.DailyProgram;
import bf.bnsp.api.account.model.DailyTeam;
import bf.bnsp.api.account.model.Fonction;
import bf.bnsp.api.account.service.AgentService;
import bf.bnsp.api.account.service.DailyProgramService;
import bf.bnsp.api.account.service.FonctionService;
import bf.bnsp.api.account.service.EquipeService;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.service.CaserneService;
import bf.bnsp.api.tools.controleForm.TokenUtils;
import bf.bnsp.api.tools.dataType.EFonction;
import bf.bnsp.api.tools.mappingTools.MappingTool;
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

    private final AgentService agentService;

    private final MappingTool mappingTool;

    private final TokenUtils tokenUtils;

    public ProgramController(FonctionService dailyProgramService, DailyProgramService dailyProgramServ, CaserneService caserneService, EquipeService equipeService, AgentService agentService, MappingTool mappingTool, TokenUtils tokenUtils) {
        this.dailyProgramService = dailyProgramServ;
        this.caserneService = caserneService;
        this.equipeService = equipeService;
        this.agentService = agentService;
        this.mappingTool = mappingTool;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createDailyProgram(@RequestBody DailyProgramInitForm dailyProgramForm, @RequestHeader("Authorization") String token){
        Optional<Caserne> caserne = Optional.empty();
        if(dailyProgramForm.getCaserneId().isEmpty()) caserne = this.tokenUtils.getCaserneFromToken(token);
        else caserne = this.caserneService.findActiveCaserneById(dailyProgramForm.getCaserneId().get());
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();

        Optional<DailyProgram> response = this.dailyProgramService.createDailyProgram(dailyProgramForm, caserne.get());
        return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> getDailyProgramByDateAndCaserne(@RequestBody DailyProgramSearchForm searchForm, @RequestHeader("Authorization") String token){
        Optional<Caserne> caserne = Optional.empty();
        if(searchForm.getCaserneId().isEmpty()) caserne = this.tokenUtils.getCaserneFromToken(token);
        else caserne = this.caserneService.findActiveCaserneById(searchForm.getCaserneId().get());

        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<DailyProgram> response = this.dailyProgramService.findActiveDailyProgramByDateAndCaserne(searchForm.getDate(), caserne.get());
            return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "/team/add")
    public ResponseEntity<?> addTeamToDailyProgram(@RequestBody DailyTeamAddForm dailyProgramForm){
        Optional<DailyProgram> response = this.dailyProgramService.findActiveDailyProgramById(dailyProgramForm.getProgramId());
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.dailyProgramService.addTeamToDailyProgram(dailyProgramForm, response.get());
            return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "/team/update")
    public ResponseEntity<?> updateDailyTeam(@RequestBody DailyTeamUpdateForm teamForm){
        Optional<DailyTeam> response = this.dailyProgramService.findActiveDailyTeamById(teamForm.getTeamId());
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else{
            response = this.dailyProgramService.updateTeamComposition(teamForm, response.get());
            return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/team/{teamId}")
    public final ResponseEntity<?> getDailyTeamById(@PathVariable("teamId") long id){
        Optional<DailyTeam> response = this.dailyProgramService.findActiveDailyTeamById(id);
        return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/team/{teamId}")
    public final ResponseEntity<?> deleteDailyTeamById(@PathVariable("teamId") long id){
        Optional<DailyTeam> response = this.dailyProgramService.findActiveDailyTeamById(id);
        if(response.isEmpty()) return ResponseEntity.notFound().build();
        else {
            response = this.dailyProgramService.deleteDailyTeam(response.get());
            return response.isPresent() ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    /*@PostMapping(value = "/create")
    public ResponseEntity<?> createDailyProgram(@RequestBody DailyProgramCreationForm dailyProgramForm){
        List<Fonction> response = this.dailyProgramService.createDailyProgram(dailyProgramForm);
        return response.size() > 0 ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response), HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateDailyProgram(@RequestBody DailyProgramCreationForm dailyProgramForm){
        List<Fonction> response = this.dailyProgramService.updateDailyProgram(dailyProgramForm, dailyProgramForm.getDate());
        return response.size() > 0 ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response), HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = {"/", ""})
    public final ResponseEntity<?> getAllDailyPrograms(){
        List<Fonction> response = this.dailyProgramService.findAllDailyProgram();
        return response.size() > 0 ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response), HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/caserne/{caserneId}")
    public final ResponseEntity<?> getDailyProgramsDateByCaserne(@PathVariable("caserneId") int caserneId){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(caserneId);
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<LocalDate> response = this.dailyProgramService.findDailyProgramDateByCaserne(caserne.get());
            return response.size() > 0 ? new ResponseEntity<>(response, HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/search")
    public final ResponseEntity<?> getDailyProgramsByCaserneAndDate(@RequestBody DailyProgramKeysForm keysForm){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(keysForm.getCaserneId());
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<Fonction> response = this.dailyProgramService.findDailyProgramDetailByCaserneAndDate(caserne.get(), keysForm.getDate());
            System.out.println(response.toString());
            return response.size() > 0 ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(value = "/delete")
    public final ResponseEntity<?> deleteDailyProgramByCaserneAndDate(@RequestBody DailyProgramKeysForm keys){
        Optional<Caserne> caserne = this.caserneService.findActiveCaserneById(keys.getCaserneId());
        if(caserne.isEmpty()) return ResponseEntity.notFound().build();
        else{
            List<Fonction> response = this.dailyProgramService.deleteFonctionList(caserne.get(), keys.getDate());
            return response.size() > 0 ? new ResponseEntity<>(this.mappingTool.mappingDailyProgram(response), HttpStatus.OK) : ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/tmp/{agentId}")
    public final ResponseEntity<?> getCurrentRole(@PathVariable("agentId") int agentId){
        Optional<Agent> agent = this.agentService.findActiveAgentById(agentId);
        if(agent.isEmpty()) return ResponseEntity.notFound().build();
        else{
            Optional<EFonction> response = this.dailyProgramService.findCurrentFunctionByAgent(agent.get());
            return response.isPresent() ? new ResponseEntity<>(response.get(), HttpStatus.OK) : ResponseEntity.notFound().build();
        }
    }*/

}
