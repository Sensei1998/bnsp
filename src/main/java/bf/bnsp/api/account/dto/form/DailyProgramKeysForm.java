package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyProgramKeysForm {
    private int caserneId;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
