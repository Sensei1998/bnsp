package bf.bnsp.api.caserne.service;

import bf.bnsp.api.caserne.dto.form.CaserneCreationForm;
import bf.bnsp.api.caserne.dto.form.CaserneUpdateForm;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.CaserneType;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.caserne.repository.CaserneRepository;
import bf.bnsp.api.caserne.repository.CaserneTypeRepository;
import bf.bnsp.api.tools.controleForm.ControlFormCaserne;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CaserneService implements CaserneServiceInterface {

    @Autowired
    private CaserneRepository caserneRepository;

    @Autowired
    private CaserneTypeRepository caserneTypeRepository;

    @Autowired
    private ControlFormCaserne controlFormCaserne;

    @Autowired
    private EnginService enginService;

    @Override
    public Optional<Caserne> createCaserne(CaserneCreationForm caserneForm, Optional<Caserne> caserneParent) {
        Optional<CaserneType> caserneType = this.caserneTypeRepository.findById(caserneForm.getIdCaserneType());
        if(caserneType.isEmpty()) return Optional.empty();
        else if (!this.controlFormCaserne.controleCaserneHierarchy(caserneType.get(), caserneParent)) return Optional.empty();
        else{
            Caserne caserne = new Caserne( caserneType.get(), caserneForm.getName(), caserneForm.getCity(), caserneForm.getArea());
            if(caserneParent.isPresent()) caserne.setAffiliation(caserneParent.get());
            this.caserneRepository.save(caserne);
            return Optional.of(caserne);
        }
    }

    @Override
    public Optional<Caserne> updateCaserne(CaserneUpdateForm caserneForm, Optional<Caserne> caserneParent, Caserne caserne) {
        Optional<CaserneType> caserneType = this.caserneTypeRepository.findById(caserneForm.getIdCaserneType());
        if(caserneType.isEmpty()) return Optional.empty();
        else if (!this.controlFormCaserne.controleCaserneHierarchy(caserneType.get(), caserneParent)) return Optional.empty();
        else{
            caserne.setCaserneType(caserneType.get());
            caserne.setName(caserneForm.getName());
            caserne.setCity(caserneForm.getCity());
            caserne.setArea(caserneForm.getArea());
            if(caserneParent.isPresent()) caserne.setAffiliation(caserneParent.get());
            this.caserneRepository.save(caserne);
            return Optional.of(caserne);
        }
    }

    @Override
    public Optional<Caserne> findActiveCaserneById(int id) {
        return this.caserneRepository.findByIdAndHiddenFalse(id);
    }

    @Override
    public List<Caserne> findAllActiveCaserne() {
        return this.caserneRepository.findByHiddenFalse();
    }

    @Override
    public List<Caserne> findByCaserneTypeAndHiddenFalse(int caserneType) {
        Optional<CaserneType> type = this.caserneTypeRepository.findById(caserneType);
        return this.caserneRepository.findByCaserneTypeAndHiddenFalse(type.get());
    }

    @Override
    public List<Caserne> findActiveCaserneByAffiliation(Caserne caserne) {
        return this.caserneRepository.findByAffiliationAndHiddenFalse(caserne);
    }

    @Override
    public List<CaserneType> findAllCaserneType() {
        return this.caserneTypeRepository.findAll();
    }

    @Override
    public Optional<Caserne> deleteCaserne(Caserne caserne) {
        caserne.setHidden(true);
        this.caserneRepository.save(caserne);
        List<Engin> engins = this.enginService.findAllActiveEnginByCaserne(caserne);
        for (Engin element: engins) {
            this.enginService.deleteEngin(element);
        }
        return Optional.of(caserne);
    }
}
