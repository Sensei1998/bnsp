package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class InterventionTeamMemberData {

    private int primaryAgentId;

    private String primaryAgentMatricule;

    private String primaryAgentFirstname;

    private String primaryAgentLastname;

    private int secondaryAgentId;

    private String secondaryAgentMatricule;

    private String secondaryAgentFirstname;

    private String secondaryAgentLastname;

    private String rule;
}
