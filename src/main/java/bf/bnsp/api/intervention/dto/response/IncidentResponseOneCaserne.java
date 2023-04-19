package bf.bnsp.api.intervention.dto.response;

import bf.bnsp.api.intervention.dto.response.partialData.IncidentSheetData;
import bf.bnsp.api.intervention.model.additional.CallerInfo;
import bf.bnsp.api.intervention.model.additional.Incident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Setter
public class IncidentResponseOneCaserne {

    private int interventionId;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createAt;

    private CallerInfo appelant;

    private Incident incident;

    private int caserneId;

    private String caserneName;

    private List<IncidentSheetData> incidentFiches = new ArrayList<>();
}
