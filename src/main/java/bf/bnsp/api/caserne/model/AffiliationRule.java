package bf.bnsp.api.caserne.model;

import lombok.*;

import javax.persistence.*;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BNSP_AFFILIATION_RULE")
public class AffiliationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NonNull
    private CaserneType caserneType;

    @NonNull
    private int level;
}
