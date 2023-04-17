package bf.bnsp.api.caserne.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaserneCreationForm {

    private int idCaserneType;

    private int idCaserneParent = 0;

    private String name;

    private String city;

    private String area;

    private String email;

    private List<String> telephone;

}
