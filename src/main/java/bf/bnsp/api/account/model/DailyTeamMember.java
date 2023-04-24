package bf.bnsp.api.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_PROGRAM_TEAM_MEMBER")
public class DailyTeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonIgnore
    private LocalDate date;

    @ManyToOne
    @NonNull
    private Agent agent;

    @ManyToOne
    @NonNull
    private FonctionType fonction;

    private boolean substitute = false;

    private boolean active = false;

    private boolean hidden = false;
}
