package bf.bnsp.api.caserne.service;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.account.service.AgentService;
import bf.bnsp.api.caserne.dto.form.AffiliationCreateForm;
import bf.bnsp.api.caserne.dto.form.AffiliationListForm;
import bf.bnsp.api.caserne.dto.form.CaserneCreationForm;
import bf.bnsp.api.caserne.dto.form.CaserneUpdateForm;
import bf.bnsp.api.caserne.dto.form.partialData.AffiliationList;
import bf.bnsp.api.caserne.dto.form.partialData.AffiliationRulesData;
import bf.bnsp.api.caserne.dto.response.CaserneResponse;
import bf.bnsp.api.caserne.dto.response.partialData.CaserneParentResponse;
import bf.bnsp.api.caserne.model.*;
import bf.bnsp.api.caserne.repository.*;
import bf.bnsp.api.tools.controleForm.ControlFormCaserne;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Service
@Log4j2
public class CaserneService implements CaserneServiceInterface {

    @Autowired
    private CaserneRepository caserneRepository;

    @Autowired
    private CaserneTypeRepository caserneTypeRepository;

    @Autowired
    private AffiliationRepository affiliationRepository;

    @Autowired
    private AffiliationLinkRepository affiliationLinkRepository;

    @Autowired
    private ControlFormCaserne controlFormCaserne;

    @Autowired
    private EnginService enginService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Override
    public Optional<Caserne> createCaserne(CaserneCreationForm caserneForm, Optional<Caserne> caserneParent) {
        Optional<CaserneType> caserneType = this.caserneTypeRepository.findById(caserneForm.getIdCaserneType());
        Optional<Zone> zone = this.zoneRepository.findById(caserneForm.getCity());
        if(caserneType.isEmpty() || zone.isEmpty()) return Optional.empty();
        else{
            if(caserneParent.isPresent()){
                if(!this.checkAffiliationRules(caserneForm.getIdAffiliation(), caserneParent.get().getCaserneType(), caserneType.get())) return Optional.empty();
            }
            Caserne caserne = new Caserne( caserneType.get(), caserneForm.getName(), zone.get(), caserneForm.getArea(), String.join(";", caserneForm.getTelephone()), caserneForm.getEmail());
            this.caserneRepository.save(caserne);
            return Optional.of(caserne);
        }
    }

    @Override
    public Optional<Caserne> updateCaserne(CaserneUpdateForm caserneForm, Optional<Caserne> caserneParent, Caserne caserne) {
        Optional<CaserneType> caserneType = this.caserneTypeRepository.findById(caserneForm.getIdCaserneType());
        Optional<Zone> zone = this.zoneRepository.findById(caserneForm.getCity());
        if(caserneType.isEmpty() || zone.isEmpty()) return Optional.empty();
        else {
            if(caserneParent.isPresent()){
                if(!this.checkAffiliationRules(caserneForm.getIdAffiliation(), caserneParent.get().getCaserneType(), caserneType.get())) return Optional.empty();
            }
            else{
                caserne.setCaserneType(caserneType.get());
                caserne.setName(caserneForm.getName());
                caserne.setCity(zone.get());
                caserne.setArea(caserneForm.getArea());
                caserne.setEmail(caserneForm.getEmail());
                caserne.setPhoneNumber(String.join(";", caserneForm.getTelephone()));
                //if(caserneParent.isPresent()) caserne.setAffiliation(caserneParent.get());
                this.caserneRepository.save(caserne);
                return Optional.of(caserne);
            }
        }
        return Optional.of(caserne);
    }

    @Override
    public Optional<AffiliationLink> setAffiliationLink(Optional<Integer> affiliationId, Caserne parent, Caserne child) {
        Optional<Affiliation> targetedAffiliation = affiliationId.isEmpty() ? this.findDefaultAffiliation() : this.findActiveAffiliationById(affiliationId.get());
        if(targetedAffiliation.isEmpty()) return Optional.empty();
        else if(this.checkAffiliationRules(affiliationId, parent.getCaserneType(), child.getCaserneType())){
            Optional<AffiliationLink> tmpLink = this.affiliationLinkRepository.findByAffiliationTypeAndCaserneChild(targetedAffiliation.get(), child);
            if(tmpLink.isEmpty()) {
                tmpLink = Optional.of(new AffiliationLink(targetedAffiliation.get(), parent, child));
            }
            else{
                tmpLink.get().setCaserneParent(parent);
            }
            this.affiliationLinkRepository.save(tmpLink.get());
            return tmpLink;
        }
        else return Optional.empty();
    }

    @Override
    public Optional<AffiliationLink> findAffiliationLinkById(int id) {
        return this.affiliationLinkRepository.findById(id);
    }

    @Override
    public List<AffiliationLink> setAffiliationListLink(AffiliationListForm affiliationList) {
        Optional<Affiliation> targetedAffiliation = affiliationList.getAffiliationId().isEmpty() ? this.findDefaultAffiliation() : this.findActiveAffiliationById(affiliationList.getAffiliationId().get());
        List<AffiliationLink> links = new ArrayList<>();
        Optional<AffiliationLink> tmpLink;
        Optional<Caserne> parent;
        Optional<Caserne> child;
        if(targetedAffiliation.isEmpty()) return new ArrayList<>();
        else{
            for (AffiliationList element : affiliationList.getLink()) {
                parent = this.caserneRepository.findByIdAndHiddenFalse(element.getCaserneParent());
                child = this.caserneRepository.findByIdAndHiddenFalse(element.getCaserneChild());
                if(parent.isEmpty() || child.isEmpty()) return new ArrayList<>();
                else if(this.checkAffiliationRules(affiliationList.getAffiliationId(), parent.get().getCaserneType(), child.get().getCaserneType())){
                    tmpLink = this.affiliationLinkRepository.findByAffiliationTypeAndCaserneChild(targetedAffiliation.get(), child.get());
                    if(tmpLink.isEmpty()) {
                        tmpLink = Optional.of(new AffiliationLink(targetedAffiliation.get(), parent.get(), child.get()));
                    }
                    else{
                        tmpLink.get().setCaserneParent(parent.get());
                    }
                    links.add(tmpLink.get());
                }
            }
            this.affiliationLinkRepository.saveAll(links);
            return links;
        }
    }

    @Override
    public Optional<AffiliationLink> deleteAffiliationLink(AffiliationLink link) {
        this.affiliationLinkRepository.delete(link);
        return Optional.of(link);
    }

    @Override
    public Optional<Caserne> findActiveCaserneById(int id) {
        return this.caserneRepository.findByIdAndHiddenFalse(id);
    }

    @Override
    public Optional<CaserneResponse> findActiveCaserneResponseById(int id) {
        Optional<Caserne> caserne = this.caserneRepository.findByIdAndHiddenFalse(id);
        if(caserne.isEmpty()) return Optional.empty();
        else{
            Optional<Caserne> caserneParent = this.findParentAffiliationCaserne(Optional.empty(), caserne.get());
            CaserneResponse response = new CaserneResponse(caserne.get().getId(), Optional.empty(), caserne.get().getCaserneType(), caserne.get().getName(),
                    caserne.get().getCity(), caserne.get().getArea(), caserne.get().getPhoneNumber(), caserne.get().getEmail());
            if(caserneParent.isPresent()) response.setCaserneParent(Optional.of(new CaserneParentResponse(caserneParent.get().getId(), caserneParent.get().getName())));
            return Optional.of(response);
        }
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
    public Optional<Caserne> findParentAffiliationCaserne(Optional<Integer> affiliationId, Caserne caserne) {
        Optional<Affiliation> targetedAffiliation = affiliationId.isEmpty() ? this.findDefaultAffiliation() : this.findActiveAffiliationById(affiliationId.get());
        if(targetedAffiliation.isEmpty()) return Optional.empty();
        else{
            Optional<AffiliationLink> response = this.affiliationLinkRepository.findByAffiliationTypeAndCaserneChild(targetedAffiliation.get(), caserne);
            if(response.isEmpty()) return Optional.empty();
            else return Optional.of(response.get().getCaserneParent());
        }
    }

    @Override
    public List<Caserne> findChildrenAffiliationCaserne(Optional<Integer> affiliationId, Caserne caserne) {
        Optional<Affiliation> targetedAffiliation = affiliationId.isEmpty() ? this.findDefaultAffiliation() : this.findActiveAffiliationById(affiliationId.get());
        List<Caserne> casernes = new ArrayList<>();
        if(targetedAffiliation.isEmpty()) return new ArrayList<>();
        else{
            List<AffiliationLink> response = this.affiliationLinkRepository.findByAffiliationTypeAndCaserneParent(targetedAffiliation.get(), caserne);
            for (AffiliationLink element: response) {
                casernes.add(element.getCaserneChild());
            }
        }
        return casernes;
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
        List<Agent> agents = this.agentService.findAllActiveAgentByCaserne(caserne);
        for (Agent element: agents) {
            this.agentService.deleteAgent(element);
        }
        return Optional.of(caserne);
    }

    @Override
    public Optional<Affiliation> createAffiliation(AffiliationCreateForm affiliationForm) {
        List<AffiliationRule> rules = new ArrayList<>();
        Optional<CaserneType> caserneType;
        for (AffiliationRulesData element: affiliationForm.getRules()) {
            caserneType = this.caserneTypeRepository.findById(element.getCaserneType());
            if(caserneType.isEmpty()) return Optional.empty();
            else{
                rules.add(new AffiliationRule(caserneType.get(), element.getLevel()));
            }
        }
        Affiliation affiliation = new Affiliation(affiliationForm.getDesignation());
        affiliation.setRules(rules);
        this.affiliationRepository.save(affiliation);
        return Optional.of(affiliation);
    }

    @Override
    public Optional<Affiliation> setActiveAffiliationDefault(Affiliation affiliation) {
        Optional<Affiliation> defaultAffiliation = this.findDefaultAffiliation();
        defaultAffiliation.get().setHidden(false);
        affiliation.setHidden(true);
        this.affiliationRepository.saveAll(Arrays.asList(defaultAffiliation.get(), affiliation));
        return Optional.of(affiliation);
    }

    @Override
    public Optional<Affiliation> findActiveAffiliationById(int id) {
        return this.affiliationRepository.findByIdAndHiddenFalse(id);
    }

    @Override
    public Optional<Affiliation> findDefaultAffiliation() {
        return this.affiliationRepository.findByDefaultStatusTrue();
    }

    @Override
    public List<Affiliation> findAllActiveAffiliation() {
        return this.affiliationRepository.findByHiddenFalse();
    }

    @Override
    public Optional<Affiliation> deleteAffiliation(Affiliation affiliation) {
        if(affiliation.isDefaultStatus()) return Optional.empty();
        else{
            affiliation.setHidden(true);
            this.affiliationRepository.save(affiliation);
            return Optional.of(affiliation);
        }
    }

    @Override
    public List<Region> findAllRegions() {
        return this.regionRepository.findAll();
    }

    @Override
    public List<Zone> findAllZones() {
        return this.zoneRepository.findByOrderByZoneAsc();
    }

    private boolean checkAffiliationRules(Optional<Integer> affiliationId, CaserneType parent, CaserneType child){
        Optional<Affiliation> affiliation = affiliationId.isEmpty() ? this.findDefaultAffiliation() : this.findActiveAffiliationById(affiliationId.get());
        if(affiliation.isEmpty()) return false;
        else{
            Optional<AffiliationRule> affParent = this.affiliationRepository.findAffiliationRuleByAffiliationIdAndCaserneType(affiliation.get().getId(), parent);
            Optional<AffiliationRule> affChild = this.affiliationRepository.findAffiliationRuleByAffiliationIdAndCaserneType(affiliation.get().getId(), child);
            return affParent.get().getLevel() == (affChild.get().getLevel() - 1);
        }
    }
}
