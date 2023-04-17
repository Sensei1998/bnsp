package bf.bnsp.api.intervention.dto.form.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterventionCallerInfo {

    private String provenance;

    private String phoneNumber;

    private String name;

    private String address;

    private float longitude;

    private float latitude;

    private String precision;


}
