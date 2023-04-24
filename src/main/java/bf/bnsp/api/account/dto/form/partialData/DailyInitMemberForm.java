package bf.bnsp.api.account.dto.form.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyInitMemberForm {

    private int principal;

    private int remplacant;
}
