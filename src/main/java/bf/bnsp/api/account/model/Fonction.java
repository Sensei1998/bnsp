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
    @JoinColumns({
        @JoinColumn(name = "dailySheet_keys_date"), @JoinColumn(name = "dailySheet_keys_caserne")
    })
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
