package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipeCreationForm {

    private int caserneId;

    private int equipeTypeId;

    private int enginId = -1;

    private String designation;

}
