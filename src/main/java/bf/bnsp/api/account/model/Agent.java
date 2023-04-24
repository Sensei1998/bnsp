package bf.bnsp.api.account.model;

import bf.bnsp.api.caserne.model.Caserne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * @author Berickal
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BNSP_AGENT")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(unique = true)
    private String matricule;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    @NonNull
    @JsonIgnore
    private String password;

    @NonNull
    private String phoneNumber;

    @Column(unique = true)
    @NonNull
    private String email;

    @NonNull
    @ManyToOne
    private Caserne caserne;

    @NonNull
    @ManyToOne
    private Grade grade;

    @ManyToOne
    private FonctionType defaultFonction;

    @JsonIgnore
    private boolean hidden = false;
}
