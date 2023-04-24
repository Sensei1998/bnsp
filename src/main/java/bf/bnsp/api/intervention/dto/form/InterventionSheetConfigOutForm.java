package bf.bnsp.api.intervention.dto.form;

import bf.bnsp.api.intervention.dto.form.partialData.InterventionSheetOut;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionSheetConfigOutForm {

    private int interventionId;

    private int caserneId;

    List<InterventionSheetOut> interventionSheet;

}
