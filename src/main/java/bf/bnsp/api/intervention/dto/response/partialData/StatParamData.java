package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatParamData {
    private long nonAttribue;

    private long enAttente;

    private long enCours;

    private long termine;
}
