package bf.bnsp.api.caserne.dto.form;

import bf.bnsp.api.caserne.dto.form.partialData.AffiliationList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffiliationListForm {

    private Optional<Integer> affiliationId;

    private List<AffiliationList> link = new ArrayList<>();
}
