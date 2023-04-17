package bf.bnsp.api.intervention.dto.form;

import bf.bnsp.api.intervention.dto.form.partialData.InterventionCallerInfo;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionIncidentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionInitAdvancedForm {
    private int cctoId;

    private InterventionCallerInfo appelant;

    private InterventionIncidentInfo incident;

    private HashSet<Integer> casernes = new HashSet<>();
}
