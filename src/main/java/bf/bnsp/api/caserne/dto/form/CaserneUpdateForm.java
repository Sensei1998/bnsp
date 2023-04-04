package bf.bnsp.api.caserne.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaserneUpdateForm {

    private int id;

    private int idCaserneType;

    private int idCaserneParent = 0;

    private String name;

    private String city;

    private String area;

}
