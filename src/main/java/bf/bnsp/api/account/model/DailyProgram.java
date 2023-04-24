package bf.bnsp.api.account.model;

import bf.bnsp.api.caserne.model.Caserne;
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
@Table(name = "BNSP_PROGRAM")
public class DailyProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @NonNull
    private Caserne caserne;

    @NonNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @NonNull
    @OneToMany(cascade = CascadeType.ALL)
    private List<DailyTeam> teams = new ArrayList<>();

    @JsonIgnore
    private boolean hidden = false;
}
