package bf.bnsp.api.caserne.model;

import bf.bnsp.api.tools.dataType.EEnginType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * @author Berickal
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BNSP_ENGIN_TYPE")
public class EnginType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @NonNull
    private EEnginType enginType;
}
