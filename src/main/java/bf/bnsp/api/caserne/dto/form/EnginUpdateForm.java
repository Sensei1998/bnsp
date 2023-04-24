package bf.bnsp.api.caserne.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnginUpdateForm {

    private int enginId;

    private int caserneId;

    private int enginTypeId;

    private String immatriculation;

    private String description;

}
