package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Equipe;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INTERVENTION_MESSAGE")
public class InterventionSheetToMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NonNull
    private InterventionSheet interventionSheet;

    @ManyToOne
    @NonNull
    private Equipe equipe;

    @NonNull
    private String message;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sentAt = LocalDateTime.now();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime receiveAt;
}
