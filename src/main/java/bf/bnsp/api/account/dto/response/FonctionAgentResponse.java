package bf.bnsp.api.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class FonctionAgentResponse {

    private long linkId;

    private int agentId;

    private String fonctionType;

    private String firstname;

    private String lastname;

    private String grade;
}
