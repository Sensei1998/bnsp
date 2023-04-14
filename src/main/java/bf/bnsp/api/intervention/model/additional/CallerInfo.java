package bf.bnsp.api.intervention.model.additional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CallerInfo {

    private String provenance;

    private String phoneNumber;

    private String name;

    private String address;

    @Embedded
    private Localisation localisation;

}
