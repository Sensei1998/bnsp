package bf.bnsp.api.intervention.model;

import bf.bnsp.api.account.model.Agent;
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
@Table(name = "BNSP_PERSON")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    private String address;

    @ManyToOne
    @JsonIgnore
    @NonNull
    private Agent createBy;
}
