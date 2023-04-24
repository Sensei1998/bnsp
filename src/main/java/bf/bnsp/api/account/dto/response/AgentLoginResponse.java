package bf.bnsp.api.account.dto.response;

import bf.bnsp.api.account.dto.response.partialData.LoginAgentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@Setter
public class AgentLoginResponse {

    private String status;

    private String token;

    private Optional<LoginAgentInfo> agent;

}
