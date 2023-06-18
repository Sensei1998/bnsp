package bf.bnsp.api.tools;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.model.EquipeType;
import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.account.repository.AgentRepository;
import bf.bnsp.api.account.repository.EquipeTypeRepository;
import bf.bnsp.api.account.repository.GradeRepository;
import bf.bnsp.api.caserne.model.*;
import bf.bnsp.api.account.model.FonctionType;
import bf.bnsp.api.caserne.repository.*;
import bf.bnsp.api.account.repository.FonctionTypeRepository;
import bf.bnsp.api.intervention.model.CategoryIncident;
import bf.bnsp.api.intervention.model.IncidentType;
import bf.bnsp.api.intervention.repository.CategoryIncidentRepository;
import bf.bnsp.api.security.PasswordEncryption;
import bf.bnsp.api.tools.dataType.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Service
@AllArgsConstructor
public class DBInit implements CommandLineRunner {

    @Autowired
    private FonctionTypeRepository ruleRepository;

    @Autowired
    private CaserneTypeRepository caserneTypeRepository;

    @Autowired
    private EnginTypeRepository enginTypeRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EquipeTypeRepository equipeTypeRepository;

    @Autowired
    private AffiliationRepository affiliationRepository;

    @Autowired
    private AffiliationLinkRepository affiliationLinkRepository;

    @Autowired
    private CategoryIncidentRepository categoryIncidentRepository;

    @Autowired
    private CaserneRepository caserneRepository;

    @Autowired
    private AgentRepository agentRepository;

    private PasswordEncryption encryption;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Override
    public void run(String... args) throws Exception {
        this.initRules();;
        this.initCaserneType();
        this.initEnginType();
        this.initGrade();
        this.initEquipeType();
        this.initData();
        this.initIncident();
        this.initZone();
        this.initCaserne();
    }

    private void initRules(){
        for (EFonction element: EFonction.values()) {
            this.ruleRepository.save(new FonctionType(element));
        }
    }

    private void initCaserneType(){
        for (ECaserneType element: ECaserneType.values()) {
            this.caserneTypeRepository.save(new CaserneType(element));
        }
    }

    private void initEnginType(){
        for (EEnginType element: EEnginType.values()) {
            this.enginTypeRepository.save(new EnginType(element));
        }
    }

    private void initGrade(){
        for (EGrade element: EGrade.values()) {
            this.gradeRepository.save(new Grade(element));
        }
    }

    private void initEquipeType(){
        for (EEquipeType element: EEquipeType.values()) {
            this.equipeTypeRepository.save(new EquipeType(element.getName(), element.getNbrMembers()));
        }
    }

    private void initData(){
        List<AffiliationRule> rules = new ArrayList<>();
        List<CaserneType> caserneType = this.caserneTypeRepository.findAll();
        int level = 1;
        for (CaserneType type: caserneType) {
            rules.add(new AffiliationRule(type, level));
            level++;
        }
        Affiliation affiliation = new Affiliation("Default");
        affiliation.setRules(rules);
        affiliation.setDefaultStatus(true);
        this.affiliationRepository.save(affiliation);
    }

    private void initIncident(){
        List<CategoryIncident> categoryIncidents = new ArrayList<>();
        List<IncidentType> incidentTypes = new ArrayList<>();
        String tmpCategory = ECategoryIncident.CODE_000.getCategory();
        CategoryIncident tmpIncident;
        for (ECategoryIncident element: ECategoryIncident.values()) {
            if(element.getCategory() != tmpCategory){
                tmpIncident = new CategoryIncident(tmpCategory);
                tmpIncident.setTypes(new ArrayList<>(incidentTypes));
                categoryIncidents.add(tmpIncident);
                incidentTypes.clear();
                tmpCategory = element.getCategory();
            }
            incidentTypes.add(new IncidentType(element.getType()));
        }
        this.categoryIncidentRepository.saveAll(categoryIncidents);
        CategoryIncident other = new CategoryIncident(ECategoryIncident.CODES_X01.getCategory());
        other.setTypes(Arrays.asList(new IncidentType(ECategoryIncident.CODES_X01.getType())));
        this.categoryIncidentRepository.save(other);
    }

    private void initCaserne(){
        List<Caserne> companies = new ArrayList<>();
        List<Agent> agents = new ArrayList<>();
        List<AffiliationLink> affiliationLinks = new ArrayList<>();
        Agent tmpAgent;
        Caserne brigade = new Caserne(this.caserneTypeRepository.findById(1).get(), "Brigade 1", this.zoneRepository.findById(107).get(), "Default", "+22600000000", "brigade1@bnsp.bf");
        for(int i = 0; i < 100; i++){
            companies.add(new Caserne(this.caserneTypeRepository.findById(2).get(), "Compagnie " + (i+1), this.zoneRepository.findById(107).get(), "Default", "+22600000000", "compagnie"+(i+1)+"@bnsp.bf"));
        }
        this.caserneRepository.save(brigade);
        this.caserneRepository.saveAll(companies);
        for (Caserne compagnie: companies) {
            affiliationLinks.add(new AffiliationLink(this.affiliationRepository.findById(1).get(), brigade, compagnie));
        }
        this.affiliationLinkRepository.saveAll(affiliationLinks);
        Agent ccot = new Agent("MT08002023000", "Agent000", "", encryption.encode("password"), "+22600000000", "agent0@bnsp.bf", this.caserneRepository.findById(1).get(), this.gradeRepository.findById(1).get());
        ccot.setDefaultFonction(this.ruleRepository.findById(3).get());
        for(int i = 1; i < 101; i++){
            tmpAgent = new Agent("MT08062023" + String.format("%03d", i), "Agent" + String.format("%03d", i), "", encryption.encode("password"), "+22600000" + String.format("%03d", i), "agent"+(i)+"@bnsp.bf", this.caserneRepository.findById(i+1).get(), this.gradeRepository.findById(1).get());
            tmpAgent.setDefaultFonction(this.ruleRepository.findById(4).get());
            agents.add(tmpAgent);
        }
        this.agentRepository.save(ccot);
        this.agentRepository.saveAll(agents);
    }

    private void initZone(){
        List<Zone> zones = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        String tmpRegion = EZoneCaserne.ZONE_000.getRegion();
        for(EZoneCaserne element: EZoneCaserne.values()){
            if(element.getRegion() != tmpRegion){
                regions.add(new Region(element.getRegion(), new ArrayList<>(zones)));
                zones.clear();
                tmpRegion = element.getRegion();
            }
            zones.add(new Zone(element.getZone()));
        }
        this.regionRepository.saveAll(regions);
    }
}
