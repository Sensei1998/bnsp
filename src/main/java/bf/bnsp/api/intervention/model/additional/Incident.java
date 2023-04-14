package bf.bnsp.api.intervention.model.additional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Incident {

    private String category;

    private String type;

    private String comments;

}
