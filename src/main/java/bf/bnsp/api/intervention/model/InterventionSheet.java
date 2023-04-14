package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Engin;
import bf.bnsp.api.intervention.model.additional.InterventionFollowedKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "BNSP_INTERVENTION_FOLLOWED")
public class InterventionSheet {

    @EmbeddedId
    private InterventionFollowedKey key;

    @ManyToOne
    private Agent agentBCOT;

    @ManyToMany
    private List<Engin> engins = new ArrayList<>();

}
