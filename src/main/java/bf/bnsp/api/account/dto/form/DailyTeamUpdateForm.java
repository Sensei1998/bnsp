package bf.bnsp.api.account.dto.form;

import bf.bnsp.api.account.dto.form.partialData.DailyMemberForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyTeamUpdateForm {

    private long teamId;

    private int typeId;

    private String designation;

    private Optional<Integer> enginId = Optional.empty();

    private List<DailyMemberForm> members = new ArrayList<>();
}
