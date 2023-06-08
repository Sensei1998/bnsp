package bf.bnsp.api.tools.dataType;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Berickal
 */
@Getter
@AllArgsConstructor
public enum EEquipeType {

    STATIONNAIRE("Stationnaire", 1),
    PLANTON("Planton", 1),
    CAPORAL("Caporal", 1),
    SGT("Sgt", 1),
    GARDE_REMISE("Garde remise", 1),

    //TYPE D'EQUIPE LIE AUX ENGIN
    PREMIER_SECOURS("Premier Secours", 5),
    FOURGON("Fourgon", 8),
    PREMIER_SECOURS_RELEVABLE("Premier Secours Relevable", 4),
    INTERVENTION_DIVERSE("Intervention Diverse", 2),
    ECHELLE("Echelle", 2),
    CANOT_SAUVETAGE_LEGER("Canot de Sauvetage Leger", 2),
    CITERNE("Citerne", 2);

    private String name;

    private int nbrMembers;

}
