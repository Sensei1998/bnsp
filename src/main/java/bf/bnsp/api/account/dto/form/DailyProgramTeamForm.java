package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyProgramTeamForm {

    private int equipeId;

    private List<DailyProgramFunctionForm> functionList = new ArrayList<>();
}
