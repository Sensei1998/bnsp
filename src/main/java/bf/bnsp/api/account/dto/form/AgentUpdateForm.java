package bf.bnsp.api.account.dto.form;

import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.caserne.model.Caserne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentUpdateForm {

    private int id;

    private String matricule;

    private String firstname;

    private String lastname;

    private int caserneId;

    private int gradeId;
}
