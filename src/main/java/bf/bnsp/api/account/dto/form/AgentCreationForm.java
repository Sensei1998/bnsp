package bf.bnsp.api.account.dto.form;

import bf.bnsp.api.account.model.Grade;
import bf.bnsp.api.caserne.model.Caserne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentCreationForm {

    private String matricule;

    private String firstname;

    private String lastname;

    private String password;

    private String email;

    private List<String> telephone = new ArrayList<>();

    private int caserneId;

    private int gradeId;

    private int defaultFonction = -1;
}
