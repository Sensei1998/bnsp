package bf.bnsp.api.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
public class FonctionAgentResponse {

    private long linkId;

    private int principalId;

    private String principalFirstname;

    private String principalLastname;

    private String principalGgrade;

    private int secondId;

    private String secondFirstname;

    private String secondLastname;

    private String secondGgrade;

    private String fonctionType;


}
