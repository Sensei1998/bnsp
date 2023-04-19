package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Setter
public class IncidentSheetCaserneData {
    private int caserneId;

    private String caserneName;

    private List<IncidentSheetData> incidentFiches = new ArrayList<>();
}
