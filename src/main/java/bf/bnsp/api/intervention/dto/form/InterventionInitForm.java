package bf.bnsp.api.intervention.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InterventionInitForm {

    private int cctoId;

    private String provenance;

    private String phoneNumber;

    private String name;

    private String address;

    private float longitude;

    private float latitude;

    private String precision;
}
