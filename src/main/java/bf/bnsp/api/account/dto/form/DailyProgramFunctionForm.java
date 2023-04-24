package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramFunctionForm {

    private Optional<Integer> id;

    private int functionTypeId;

    private int agentId;
}
