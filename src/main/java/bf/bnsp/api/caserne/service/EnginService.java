package bf.bnsp.api.caserne.service;

import bf.bnsp.api.caserne.dto.form.EnginCreationForm;
import bf.bnsp.api.caserne.dto.form.EnginUpdateForm;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.caserne.model.EnginType;
import bf.bnsp.api.caserne.repository.CaserneRepository;
import bf.bnsp.api.caserne.repository.EnginRepository;
import bf.bnsp.api.caserne.repository.EnginTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnginService implements EnginServiceInterface{

    @Autowired
    private EnginRepository enginRepository;

    @Autowired
    private CaserneRepository caserneRepository;

    @Autowired
    private EnginTypeRepository enginTypeRepository;

    @Override
    public Optional<Engin> createEngin(EnginCreationForm enginForm, Caserne caserne) {
        Optional<EnginType> enginType = this.enginTypeRepository.findById(enginForm.getEnginTypeId());
        if(enginType.isEmpty()) return Optional.empty();
        else{
            Engin engin = new Engin(enginForm.getDescription(), enginForm.getImmatriculation(), caserne, enginType.get());
            this.enginRepository.save(engin);
            return Optional.of(engin);
        }
    }

    @Override
    public Optional<Engin> updateEngin(EnginUpdateForm enginForm, Caserne caserne, Engin targetedEngin) {
        Optional<EnginType> enginType = this.enginTypeRepository.findById(enginForm.getEnginTypeId());
        if(enginType.isEmpty()) return Optional.empty();
        else{
            targetedEngin.setDescription(enginForm.getDescription());
            targetedEngin.setImmatriculation(enginForm.getImmatriculation());
            targetedEngin.setCaserne(caserne);
            targetedEngin.setEnginType(enginType.get());
            this.enginRepository.save(targetedEngin);
            return Optional.of(targetedEngin);
        }
    }

    @Override
    public Optional<Engin> updateEnginAvailability(Engin targetedEngin, boolean available) {
        targetedEngin.setAvailable(available);
        this.enginRepository.save(targetedEngin);
        return Optional.of(targetedEngin);
    }

    @Override
    public Optional<Engin> updateEnginOut(Engin targetedEngin, boolean out) {
        targetedEngin.setSortie(out);
        targetedEngin.setAvailable(!out);
        this.enginRepository.save(targetedEngin);
        return Optional.of(targetedEngin);
    }

    @Override
    public Optional<Engin> findActiveEnginById(int enginId) {
        return this.enginRepository.findByIdAndHiddenFalse(enginId);
    }

    @Override
    public Optional<Engin> findActiveEnginByCaserneAndId(int enginId, Caserne caserne) {
        return this.enginRepository.findByIdAndCaserneAndHiddenFalse(enginId, caserne);
    }

    @Override
    public Optional<Engin> findActiveEnginByImmatriculation(String immatriculation) {
        return this.enginRepository.findByImmatriculationAndHiddenFalse(immatriculation);
    }

    @Override
    public List<Engin> findAllActiveEngin() {
        return this.enginRepository.findByHiddenFalse();
    }

    @Override
    public List<Engin> findAllActiveEnginByCaserne(Caserne caserne) {
        return this.enginRepository.findByCaserneAndHiddenFalse(caserne);
    }

    @Override
    public List<EnginType> findAllEnginType() {
        return this.enginTypeRepository.findAll();
    }

    @Override
    public Optional<Engin> deleteEngin(Engin targetedEngin) {
        targetedEngin.setHidden(true);
        targetedEngin.setImmatriculation(targetedEngin.getImmatriculation() + "_#HIDDEN" + this.enginRepository.countByImmatriculationContains(targetedEngin.getImmatriculation()));
        this.enginRepository.save(targetedEngin);
        return Optional.of(targetedEngin);
    }


}
