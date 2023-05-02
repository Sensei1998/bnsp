package bf.bnsp.api.intervention.dto.form;

import bf.bnsp.api.intervention.dto.form.partialData.InterventionCaserne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterventionUpdateGeneralForm {

    private int id;

    private List<InterventionCaserne> casernes = new ArrayList<>();

}
