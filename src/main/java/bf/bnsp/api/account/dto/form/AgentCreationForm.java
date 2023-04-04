package bf.bnsp.api.account.dto.form;

import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.caserne.model.Caserne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentCreationForm {

    private String matricule;

    private String firstname;

    private String lastname;

    private String password;

    private int caserneId;

    private int gradeId;
}
