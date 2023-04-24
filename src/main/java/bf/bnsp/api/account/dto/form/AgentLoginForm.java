package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentLoginForm {

    private String identifier;

    private String password;
}
