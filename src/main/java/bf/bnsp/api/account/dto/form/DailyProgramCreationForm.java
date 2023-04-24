package bf.bnsp.api.account.dto.form;

import bf.bnsp.api.account.dto.form.partialData.DailyInitMemberForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramCreationForm {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date = LocalDate.now();

    private Optional<Integer> caserneId = Optional.empty();

    private Optional<DailyInitMemberForm> caporal;

    private Optional<DailyInitMemberForm> sergent;

}
