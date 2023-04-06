package bf.bnsp.api.account.service;

import bf.bnsp.api.account.dto.form.EquipeCreationForm;
import bf.bnsp.api.account.dto.form.EquipeUpdateForm;
import bf.bnsp.api.account.model.Equipe;
import bf.bnsp.api.account.model.EquipeType;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;

import java.util.List;
import java.util.Optional;

public interface EquipeServiceInterface {

    Optional<Equipe> createEquipe(EquipeCreationForm equipeForm, Caserne caserne, Optional<Engin> engin);

    Optional<Equipe> updateEquipe(EquipeUpdateForm equipeForm, Caserne caserne, Optional<Engin> engin, Equipe targetedEquipe);

    Optional<Equipe> findActiveEquipeById(int equipeId);

    List<Equipe> findAllActiveEquipe();

    List<Equipe> findAllActiveEquipeByCaserne(Caserne caserne);

    List<EquipeType> findAllEquipeType();

    List<Equipe> findAllActiveEquipeByCaserneAndType(Caserne caserne, int equipeTypeId);

    Optional<Equipe> deleteEquipe(Equipe targetedEquipe);

}
