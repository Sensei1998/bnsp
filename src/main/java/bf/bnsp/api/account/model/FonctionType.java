package bf.bnsp.api.account.model;

import bf.bnsp.api.tools.dataType.EFonction;
import lombok.*;

import javax.persistence.*;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_RULE")
public class FonctionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @NonNull
    private EFonction rule;
}
