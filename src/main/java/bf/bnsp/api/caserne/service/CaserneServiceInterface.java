package bf.bnsp.api.caserne.service;

import bf.bnsp.api.caserne.dto.form.CaserneCreationForm;
import bf.bnsp.api.caserne.dto.form.CaserneUpdateForm;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.CaserneType;

import java.util.List;
import java.util.Optional;

public interface CaserneServiceInterface {

    Optional<Caserne> createCaserne(CaserneCreationForm caserneForm, Optional<Caserne> caserneParent);

    Optional<Caserne> updateCaserne(CaserneUpdateForm brigadeForm, Optional<Caserne> caserneParent, Caserne caserne);

    Optional<Caserne> findActiveCaserneById(int id);

    List<Caserne> findAllActiveCaserne();

    List<Caserne> findByCaserneTypeAndHiddenFalse(int caserneType);

    List<Caserne> findActiveCaserneByAffiliation(Caserne caserne);

    List<CaserneType> findAllCaserneType();

    Optional<Caserne> deleteCaserne(Caserne caserne);
}
