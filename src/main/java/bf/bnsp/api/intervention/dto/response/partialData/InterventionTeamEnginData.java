package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class InterventionTeamEnginData {

    private int enginId;

    private String type;

    private String immatriculation;

}
