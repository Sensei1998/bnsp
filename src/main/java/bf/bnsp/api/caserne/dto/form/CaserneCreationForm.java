package bf.bnsp.api.caserne.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaserneCreationForm {

    private int idCaserneType;

    private Optional<Integer> idCaserneParent = Optional.empty();

    private Optional<Integer> idAffiliation = Optional.empty();

    private String name;

    private String city;

    private String area;

    private String email;

    private List<String> telephone;

}
