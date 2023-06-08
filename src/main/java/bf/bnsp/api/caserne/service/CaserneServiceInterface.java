package bf.bnsp.api.caserne.service;

import bf.bnsp.api.caserne.dto.form.AffiliationCreateForm;
import bf.bnsp.api.caserne.dto.form.AffiliationListForm;
import bf.bnsp.api.caserne.dto.form.CaserneCreationForm;
import bf.bnsp.api.caserne.dto.form.CaserneUpdateForm;
import bf.bnsp.api.caserne.model.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
public interface CaserneServiceInterface {

    Optional<Caserne> createCaserne(CaserneCreationForm caserneForm, Optional<Caserne> caserneParent);

    Optional<Caserne> updateCaserne(CaserneUpdateForm brigadeForm, Optional<Caserne> caserneParent, Caserne caserne);

    Optional<AffiliationLink> setAffiliationLink(Optional<Integer> affiliationId, Caserne parent, Caserne child);

    Optional<AffiliationLink> findAffiliationLinkById(int id);

    List<AffiliationLink> setAffiliationListLink(AffiliationListForm affiliationList);

    Optional<AffiliationLink> deleteAffiliationLink(AffiliationLink link);

    Optional<Caserne> findActiveCaserneById(int id);

    List<Caserne> findAllActiveCaserne();

    List<Caserne> findByCaserneTypeAndHiddenFalse(int caserneType);

    Optional<Caserne> findParentAffiliationCaserne(Optional<Integer> affiliationId, Caserne caserne);

    List<Caserne> findChildrenAffiliationCaserne(Optional<Integer> affiliationId, Caserne caserne);

    List<CaserneType> findAllCaserneType();

    Optional<Caserne> deleteCaserne(Caserne caserne);

    Optional<Affiliation> createAffiliation(AffiliationCreateForm affiliationForm);

    Optional<Affiliation> setActiveAffiliationDefault(Affiliation affiliation);

    Optional<Affiliation> findActiveAffiliationById(int id);

    Optional<Affiliation> findDefaultAffiliation();

    List<Affiliation> findAllActiveAffiliation();

    Optional<Affiliation> deleteAffiliation(Affiliation affiliation);

    List<Region> findAllRegions();

    List<Zone> findAllZones();
}
