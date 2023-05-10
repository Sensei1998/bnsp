package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Setter
public class InterventionCaserneData {

    private int caserneId;

    private String caserneName;

    private String message;

    private List<InterventionTeamData> teams = new ArrayList<>();
}
