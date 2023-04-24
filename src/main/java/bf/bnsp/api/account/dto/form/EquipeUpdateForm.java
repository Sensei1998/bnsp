package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipeUpdateForm {

    private int equipeId;

    private int caserneId;

    private int equipeTypeId;

    private int enginId = -1;

    private String designation;
}
