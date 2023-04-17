package bf.bnsp.api.intervention.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionSheetMessageForm {

    private int interventionId;

    private int caserneId;

    private int equipeId;

    private String message;
}
