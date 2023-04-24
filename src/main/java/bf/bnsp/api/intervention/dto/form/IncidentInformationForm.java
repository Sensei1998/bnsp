package bf.bnsp.api.intervention.dto.form;

import bf.bnsp.api.intervention.dto.form.partialData.PersonInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentInformationForm {

    private int interventionId;

    private Optional<Integer> caserneId = Optional.empty();

    private List<PersonInfo> proprietaires = new ArrayList<>();

    private List<PersonInfo> sinistres = new ArrayList<>();

    private String degats;

    private String commentaires;
}
