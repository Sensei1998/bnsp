package bf.bnsp.api.caserne.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "BNSP_CASERNE")
public class Caserne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne
    private CaserneType caserneType;

    @NonNull
    private String name;

    @NonNull
    @ManyToOne
    private Zone city;

    @NonNull
    private String area;

    @NonNull
    private String phoneNumber;

    @NonNull
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private boolean hidden = false;
}
