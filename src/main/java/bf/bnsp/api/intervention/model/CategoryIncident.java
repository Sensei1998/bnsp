package bf.bnsp.api.intervention.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "BNSP_INTERVENTION_CATEGORY")
public class CategoryIncident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String category;

    @OneToMany(cascade = CascadeType.ALL)
    @NonNull
    @JsonIgnore
    private List<IncidentType> types = new ArrayList<>();
}
