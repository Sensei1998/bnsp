package bf.bnsp.api.account.dto.form.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTeamForm {

    private int typeId;

    private String designation;

    private Optional<Integer> enginId = Optional.empty();

    private List<DailyMemberForm> members = new ArrayList<>();
}
