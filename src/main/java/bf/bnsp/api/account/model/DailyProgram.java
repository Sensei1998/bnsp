package bf.bnsp.api.account.model;

import bf.bnsp.api.caserne.model.Caserne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BNSP_DAILY_PROGRAM")
public class DailyProgram {

    @EmbeddedId
    private DailyProgramKeys keys;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "dailySheet")
    private List<Fonction> fonctions;
}
