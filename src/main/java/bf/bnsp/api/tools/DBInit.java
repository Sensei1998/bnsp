package bf.bnsp.api.tools;

import bf.bnsp.api.account.model.EquipeType;
import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.account.repository.EquipeTypeRepository;
import bf.bnsp.api.account.repository.GradeRepository;
import bf.bnsp.api.caserne.model.CaserneType;
import bf.bnsp.api.caserne.model.EnginType;
import bf.bnsp.api.account.model.FonctionType;
import bf.bnsp.api.caserne.repository.CaserneTypeRepository;
import bf.bnsp.api.caserne.repository.EnginTypeRepository;
import bf.bnsp.api.account.repository.FonctionTypeRepository;
import bf.bnsp.api.tools.dataType.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

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

    @Override
    public void run(String... args) throws Exception {
        this.initRules();;
        this.initCaserneType();
        this.initEnginType();
        this.initGrade();
        this.initEquipeType();
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
}
