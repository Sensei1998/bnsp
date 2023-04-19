package bf.bnsp.api.account.dto.response;

import bf.bnsp.api.account.dto.response.partialData.TeamInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Setter
public class TeamResponse {

    private int caserneId;

    private String caserneName;

    private String caserneCity;

    private String caserneArea;

    private List<TeamInfo> teams = new ArrayList<>();
}
