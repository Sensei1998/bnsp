package bf.bnsp.api.intervention.dto.response.partialData;

import bf.bnsp.api.intervention.dto.form.partialData.PersonInfo;
import bf.bnsp.api.intervention.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@Setter
public class IncidentSheetData {

    private List<Person> proprietaires;

    private List<Person> victimes;

    private String degats;

    private String commentaires;
}
