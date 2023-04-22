package bf.bnsp.api.caserne.dto.form.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffiliationRulesData {

    private int caserneType;

    private int level;
}
