package bf.bnsp.api.caserne.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BNSP_AFFILIATION_LINK")
public class AffiliationLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NonNull
    private Affiliation affiliationType;

    @ManyToOne
    @NonNull
    private Caserne caserneParent;

    @ManyToOne
    @NonNull
    private Caserne caserneChild;
}
