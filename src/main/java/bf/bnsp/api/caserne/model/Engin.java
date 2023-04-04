package bf.bnsp.api.caserne.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BNSP_ENGIN")
public class Engin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String description;

    @NonNull
    private String immatriculation;

    @ManyToOne
    @NonNull
    private Caserne caserne;

    @ManyToOne
    @NonNull
    private EnginType enginType;

    private boolean sortie = false;

    private boolean available = true;

    private boolean hidden = false;
}
