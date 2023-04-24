package bf.bnsp.api.account.model;

import bf.bnsp.api.caserne.model.Engin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_PROGRAM_TEAM")
public class DailyTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonIgnore
    private LocalDate date;

    @ManyToOne
    @NonNull
    private EquipeType type;

    @NonNull
    private String designation;

    @ManyToOne
    private Engin engin;

    @OneToMany(cascade = CascadeType.ALL)
    @NonNull
    private List<DailyTeamMember> members = new ArrayList<>();

    @JsonIgnore
    private boolean active = false;

    @JsonIgnore
    private boolean hidden = false;
}
