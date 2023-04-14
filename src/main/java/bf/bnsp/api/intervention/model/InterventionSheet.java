package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_INTERVENTION_FOLLOWED")
public class InterventionSheet {

    @EmbeddedId
    private InterventionFollowedKey key;

    @ManyToOne
    private Agent agentBCOT;

    @ManyToMany
    private HashSet<Engin> engins = new HashSet<>();

}
