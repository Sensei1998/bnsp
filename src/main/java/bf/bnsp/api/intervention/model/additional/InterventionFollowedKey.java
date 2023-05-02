package bf.bnsp.api.intervention.model.additional;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.model.Intervention;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class InterventionFollowedKey implements Serializable {

    @ManyToOne
    private Intervention intervention;

    @ManyToOne
    @JsonIgnore
    private Caserne caserne;
}
