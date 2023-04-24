package bf.bnsp.api.caserne.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BNSP_AFFILIATION")
public class Affiliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String designation;

    @OneToMany(cascade = CascadeType.ALL)
    @NonNull
    private List<AffiliationRule> rules = new ArrayList<>();

    private boolean defaultStatus = false;

    private boolean hidden = false;
}
