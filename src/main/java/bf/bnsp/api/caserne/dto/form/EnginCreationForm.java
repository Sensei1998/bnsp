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
public class EnginCreationForm {

    private int caserneId;

    private int enginTypeId;

    private String immatriculation;

    private String description;

}
