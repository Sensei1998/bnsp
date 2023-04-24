package bf.bnsp.api.caserne.dto.form;

import bf.bnsp.api.caserne.dto.form.partialData.AffiliationRulesData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffiliationCreateForm {

    private String designation;

    private List<AffiliationRulesData> rules;
}
