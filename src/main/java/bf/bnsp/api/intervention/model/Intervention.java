package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Agent;
import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.model.additional.CallerInfo;
import bf.bnsp.api.intervention.model.additional.Incident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BNSP_INTERVENTION")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @ManyToOne
    private Agent agentCCOT;

    @Embedded
    private CallerInfo caller;

    @Embedded
    private Incident incident;

    private boolean done = false;

    private boolean hidden = false;
}
