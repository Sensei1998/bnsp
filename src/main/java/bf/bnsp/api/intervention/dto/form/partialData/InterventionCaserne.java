package bf.bnsp.api.intervention.dto.form.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionCaserne {

    private int caserneId;

    private String message;
}
