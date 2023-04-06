package bf.bnsp.api.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_FONCTION")
public class Fonction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "BNSP_DAILY_PROGRAM_id")
    private DailyProgram dailySheet;

    @NonNull
    @ManyToOne
    private Equipe equipe;

    @NonNull
    @ManyToOne
    private FonctionType function;

    @NonNull
    @ManyToOne
    private Agent agent;
}
