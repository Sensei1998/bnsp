package bf.bnsp.api.intervention.model.additional;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.model.Intervention;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InterventionFollowedKey {

    private Intervention intervention;

    private Caserne caserne;
}
