package bf.bnsp.api.account.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAgentInfo {

    private int id;

    private String matricule;

    private String firstname;

    private String lastname;

    private String email;

    private List<String> phoneNumber;

    private String gradeName;

    private String currentFunction;

    private int caserneId;

}
