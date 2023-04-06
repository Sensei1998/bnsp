package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.EquipeCreationForm;
import bf.bnsp.api.account.dto.form.EquipeUpdateForm;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.model.EquipeType;
import bf.bnsp.api.account.repository.EquipeRepository;
import bf.bnsp.api.account.repository.EquipeTypeRepository;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EquipeService implements EquipeServiceInterface{

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private EquipeTypeRepository equipeTypeRepository;

    @Override
    public Optional<Equipe> createEquipe(EquipeCreationForm equipeForm, Caserne caserne, Optional<Engin> engin) {
        Optional<EquipeType> equipeType = this.equipeTypeRepository.findById(equipeForm.getEquipeTypeId());
        if(equipeType.isEmpty()) return Optional.empty();
        else{
            Equipe equipe = new Equipe(caserne, equipeForm.getDesignation(), equipeType.get());
            if(engin.isPresent()) equipe.setEngin(engin.get());
            this.equipeRepository.save(equipe);
            return Optional.of(equipe);
        }
    }

    @Override
    public Optional<Equipe> updateEquipe(EquipeUpdateForm equipeForm, Caserne caserne, Optional<Engin> engin, Equipe targetedEquipe) {
        Optional<EquipeType> equipeType = this.equipeTypeRepository.findById(equipeForm.getEquipeTypeId());
        if(equipeType.isEmpty()) return Optional.empty();
        else{
            targetedEquipe.setDesignation(equipeForm.getDesignation());
            targetedEquipe.setCaserne(caserne);
            targetedEquipe.setEquipeType(equipeType.get());
            targetedEquipe.setEngin(engin.isPresent() ? engin.get() : null);
            this.equipeRepository.save(targetedEquipe);
            return Optional.of(targetedEquipe);
        }
    }

    @Override
    public Optional<Equipe> findActiveEquipeById(int equipeId) {
        return this.equipeRepository.findByIdAndHiddenFalse(equipeId);
    }

    @Override
    public List<Equipe> findAllActiveEquipe() {
        return this.equipeRepository.findByHiddenFalse();
    }

    @Override
    public List<Equipe> findAllActiveEquipeByCaserne(Caserne caserne) {
        return this.equipeRepository.findByCaserneAndHiddenFalse(caserne);
    }

    @Override
    public List<EquipeType> findAllEquipeType() {
        return this.equipeTypeRepository.findAll();
    }

    @Override
    public List<Equipe> findAllActiveEquipeByCaserneAndType(Caserne caserne, int equipeTypeId) {
        Optional<EquipeType> equipeType = this.equipeTypeRepository.findById(equipeTypeId);
        if(equipeType.isEmpty()) return new ArrayList<>();
        else return this.equipeRepository.findByCaserneAndEquipeTypeAndHiddenFalse(caserne, equipeType.get());
    }

    @Override
    public Optional<Equipe> deleteEquipe(Equipe targetedEquipe) {
        targetedEquipe.setHidden(true);
        this.equipeRepository.save(targetedEquipe);
        return Optional.of(targetedEquipe);
    }
}
