package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Agent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Berickal
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BNSP_SINISTER")
public class Sinister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NonNull
    @JsonIgnore
    private InterventionSheet interventionSheet;

    @OneToMany(cascade = CascadeType.ALL)
    @NonNull
    private List<Person> owners = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @NonNull
    private List<Person> victims = new ArrayList<>();

    @NonNull
    private String damages;

    @NonNull
    private String comments;

    @ManyToOne
    @JsonIgnore
    private Agent updateBy;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonIgnore
    private LocalDateTime createAt;
}
