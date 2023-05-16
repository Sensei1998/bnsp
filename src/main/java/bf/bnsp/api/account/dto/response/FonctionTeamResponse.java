package bf.bnsp.api.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
public class FonctionTeamResponse {

    private long teamId;

    private String teamType;

    private String teamName;

    private List<FonctionAgentResponse> agents;

    private boolean available;
}
