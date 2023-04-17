package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_INTERVENTION_FOLLOWED")
public class InterventionSheet {

    @EmbeddedId
    @NonNull
    private InterventionFollowedKey key;

    @ManyToOne
    private Agent agentBCOT;

    @JsonIgnore
    private boolean active = false;

    @JsonIgnore
    private boolean hidden = false;

}
