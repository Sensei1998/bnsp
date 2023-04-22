package bf.bnsp.api.tools;

import bf.bnsp.api.account.model.EquipeType;
import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.account.repository.EquipeTypeRepository;
import bf.bnsp.api.account.repository.GradeRepository;
import bf.bnsp.api.caserne.model.Affiliation;
import bf.bnsp.api.caserne.model.AffiliationRule;
import bf.bnsp.api.caserne.model.CaserneType;
import bf.bnsp.api.caserne.model.EnginType;
import bf.bnsp.api.account.model.FonctionType;
import bf.bnsp.api.caserne.repository.AffiliationRepository;
import bf.bnsp.api.caserne.repository.CaserneTypeRepository;
import bf.bnsp.api.caserne.repository.EnginTypeRepository;
import bf.bnsp.api.account.repository.FonctionTypeRepository;
import bf.bnsp.api.intervention.model.CategoryIncident;
import bf.bnsp.api.intervention.model.IncidentType;
import bf.bnsp.api.intervention.repository.CategoryIncidentRepository;
import bf.bnsp.api.tools.dataType.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private CategoryIncidentRepository categoryIncidentRepository;

    @Override
    public void run(String... args) throws Exception {
        this.initRules();;
        this.initCaserneType();
        this.initEnginType();
        this.initGrade();
        this.initEquipeType();
        this.initData();
        this.initIncident();
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
            this.equipeTypeRepository.save(new EquipeType(element));
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
                tmpIncident = new CategoryIncident(element.getCategory());
                tmpIncident.setTypes(new ArrayList<>(incidentTypes));
                categoryIncidents.add(tmpIncident);
                incidentTypes.clear();
                tmpCategory = element.getCategory();
            }
            incidentTypes.add(new IncidentType(element.getType()));
        }
        this.categoryIncidentRepository.saveAll(categoryIncidents);
    }
}
