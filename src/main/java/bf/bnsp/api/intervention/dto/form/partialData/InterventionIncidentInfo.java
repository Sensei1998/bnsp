package bf.bnsp.api.intervention.dto.form.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterventionIncidentInfo {

    private Optional<Integer> categoryId;

    private int incidentTypeId;

    private String comments;
}
