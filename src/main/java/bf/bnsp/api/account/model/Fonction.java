package bf.bnsp.api.account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_FONCTION")
public class Fonction {

    @EmbeddedId
    @NonNull
    private FonctionKeys keys;

    @ManyToOne
    @NonNull
    private FonctionType function;

    @ManyToOne
    @NonNull
    private Equipe equipe;

    private boolean hidden = false;
}
