package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramSearchForm {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private Optional<Integer> caserneId = Optional.empty();
}
