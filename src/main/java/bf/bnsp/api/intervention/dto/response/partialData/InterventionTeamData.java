package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@Setter
public class InterventionTeamData {

    private long teamId;

    private String type;

    private Optional<InterventionTeamEnginData> engin = Optional.empty();

    private List<InterventionTeamMemberData> members = new ArrayList<>();
}
