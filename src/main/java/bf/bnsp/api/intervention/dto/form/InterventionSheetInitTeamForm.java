package bf.bnsp.api.intervention.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionSheetInitTeamForm {

    private int interventionId;

    private HashSet<Integer> equipes;

}
