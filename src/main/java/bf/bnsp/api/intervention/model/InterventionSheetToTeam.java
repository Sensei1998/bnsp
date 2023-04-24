package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.DailyTeam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Berickal
 */
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "INTERVENTION_ENGIN")
public class InterventionSheetToTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NonNull
    private InterventionSheet interventionSheet;

    @ManyToOne
    @NonNull
    private DailyTeam equipe;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createAt;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime departure;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime presentation;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime available;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime checkIn;

    @JsonIgnore
    private boolean active = false;

    @JsonIgnore
    private boolean hidden = false;

}
