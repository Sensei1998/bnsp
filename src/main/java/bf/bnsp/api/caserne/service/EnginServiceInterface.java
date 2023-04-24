package bf.bnsp.api.caserne.service;

import bf.bnsp.api.caserne.dto.form.EnginCreationForm;
import bf.bnsp.api.caserne.dto.form.EnginUpdateForm;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.caserne.model.EnginType;

import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
public interface EnginServiceInterface {

    Optional<Engin> createEngin(EnginCreationForm enginForm, Caserne caserne);

    Optional<Engin> updateEngin(EnginUpdateForm enginForm, Caserne caserne, Engin targetedEngin);

    Optional<Engin> updateEnginAvailability(Engin targetedEngin, boolean available);

    Optional<Engin> updateEnginOut(Engin targetedEngin, boolean out);

    Optional<Engin> findActiveEnginById(int enginId);

    Optional<Engin> findActiveEnginByCaserneAndId(int enginId, Caserne caserne);

    Optional<Engin> findActiveEnginByImmatriculation(String immatriculation);

    List<Engin> findAllActiveEngin();

    List<Engin> findAllActiveEnginByCaserne(Caserne caserne);

    List<EnginType> findAllEnginType();

    Optional<Engin> deleteEngin(Engin targetedEngin);
}
