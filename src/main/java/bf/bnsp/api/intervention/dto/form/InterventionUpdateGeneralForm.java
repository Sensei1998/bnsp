package bf.bnsp.api.intervention.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterventionUpdateGeneralForm {

    private int id;

    private String category;

    private String libelle;

    private String comments;

    private HashSet<Integer> caserneId = new HashSet<>();

}
