package bf.bnsp.api.account.model;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.caserne.model.Engin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_TEAM")
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @ManyToOne
    private Caserne caserne;

    @OneToOne
    private Engin engin;

    @NonNull
    private String designation;

    @ManyToOne
    @NonNull
    private EquipeType equipeType;

    @JsonIgnore
    private boolean hidden = false;

    private boolean available = true;
}
