package bf.bnsp.api.tools.controleForm;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.CaserneType;
import bf.bnsp.api.caserne.repository.CaserneTypeRepository;
import bf.bnsp.api.tools.dataType.ECaserneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Berickal
 */
@Service
public class ControlFormCaserne {

    @Autowired
    private CaserneTypeRepository caserneTypeRepository;

    public boolean controleCaserneHierarchy(CaserneType caserneType, Optional<Caserne> caserneParent){
        if(caserneParent.isEmpty() && caserneType.getCaserneType() == ECaserneType.BRIGADE) return true;

        else if ((caserneParent.get().getCaserneType().getCaserneType() == ECaserneType.BRIGADE && caserneType.getCaserneType() == ECaserneType.COMPAGNIE) ||
                (caserneParent.get().getCaserneType().getCaserneType() == ECaserneType.COMPAGNIE && caserneType.getCaserneType() == ECaserneType.CENTRE_SECOURS))
            return true;

        return false;
    }
}
