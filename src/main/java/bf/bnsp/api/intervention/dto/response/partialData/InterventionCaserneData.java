package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class InterventionCaserneData {

    private int caserneId;

    private String caserneName;

    private String message;
}
