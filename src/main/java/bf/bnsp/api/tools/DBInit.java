package bf.bnsp.api.tools;

import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.account.repository.GradeRepository;
import bf.bnsp.api.caserne.model.CaserneType;
import bf.bnsp.api.caserne.model.EnginType;
import bf.bnsp.api.caserne.model.Rule;
import bf.bnsp.api.caserne.repository.CaserneTypeRepository;
import bf.bnsp.api.caserne.repository.EnginTypeRepository;
import bf.bnsp.api.caserne.repository.RuleRepository;
import bf.bnsp.api.tools.dataType.ECaserneType;
import bf.bnsp.api.tools.dataType.EEnginType;
import bf.bnsp.api.tools.dataType.EGrade;
import bf.bnsp.api.tools.dataType.ERule;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class DBInit implements CommandLineRunner {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private CaserneTypeRepository caserneTypeRepository;

    @Autowired
    private EnginTypeRepository enginTypeRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public void run(String... args) throws Exception {
        this.initRules();;
        this.initCaserneType();
        this.initEnginType();
        this.initGrade();
    }

    private void initRules(){
        Rule rule1 = new Rule(ERule.ROLE_SUPERVISOR);
        Rule rule2 = new Rule(ERule.ROLE_PRIMARY_STATIONARY);
        Rule rule3 = new Rule(ERule.ROLE_SECONDARY_STATIONARY);

        List<Rule> rulesList = Arrays.asList(rule1, rule2, rule3);
        this.ruleRepository.saveAll(rulesList);
    }

    private void initCaserneType(){
        CaserneType caserneType1 = new CaserneType(ECaserneType.BRIGADE);
        CaserneType caserneType2 = new CaserneType(ECaserneType.COMPAGNIE);
        CaserneType caserneType3 = new CaserneType(ECaserneType.CENTRE_SECOURS);
        List<CaserneType> caserneTypeList = Arrays.asList(caserneType1, caserneType2, caserneType3);
        this.caserneTypeRepository.saveAll(caserneTypeList);
    }

    private void initEnginType(){
        EnginType enginType1 = new EnginType(EEnginType.FOURGON);
        EnginType enginType2 = new EnginType(EEnginType.ECHELLE);
        List<EnginType> enginTypes = Arrays.asList(enginType1, enginType2);
        this.enginTypeRepository.saveAll(enginTypes);
    }

    private void initGrade(){
        for (EGrade element: EGrade.values()) {
            this.gradeRepository.save(new Grade(element));
        }
    }
}
