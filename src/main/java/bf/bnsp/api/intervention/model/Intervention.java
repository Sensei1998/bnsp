package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.model.additional.CallerInfo;
import bf.bnsp.api.intervention.model.additional.Incident;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_INTERVENTION")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne
    @NonNull
    private Agent agentCCOT;

    @Embedded
    @NonNull
    private CallerInfo caller;

    @Embedded
    private Incident incident;

    private String status = "Non attribué";

    private boolean done = false;

    @JsonIgnore
    private boolean hidden = false;
}
