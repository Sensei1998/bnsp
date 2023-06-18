package bf.bnsp.api.caserne.dto.response;

import bf.bnsp.api.caserne.dto.response.partialData.CaserneParentResponse;
import bf.bnsp.api.caserne.model.CaserneType;
import bf.bnsp.api.caserne.model.Zone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.util.Optional;

@Data
@AllArgsConstructor
@Setter
public class CaserneResponse {
    private int id;

    Optional<CaserneParentResponse> caserneParent = Optional.empty();

    private CaserneType caserneType;

    private String name;

    private Zone city;

    private String area;

    private String phoneNumber;

    private String email;
}
