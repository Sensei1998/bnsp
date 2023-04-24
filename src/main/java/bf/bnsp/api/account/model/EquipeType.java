package bf.bnsp.api.account.model;

import bf.bnsp.api.tools.dataType.EEquipeType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_TEAM_TYPE")
public class EquipeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @NonNull
    private EEquipeType equipeType;
}
