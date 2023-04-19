package bf.bnsp.api.account.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.Optional;

@Data
@AllArgsConstructor
@Setter
public class TeamInfo {

    private int teamId;

    private String teamName;

    private String teamType;

    private Optional<EnginInfo> engin;
}
