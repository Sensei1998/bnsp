package bf.bnsp.api.caserne.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Caserne affiliation;

    @NonNull
    private String name;

    @NonNull
    private String city;

    @NonNull
    private String area;

    @JsonIgnore
    private boolean hidden = false;
}
