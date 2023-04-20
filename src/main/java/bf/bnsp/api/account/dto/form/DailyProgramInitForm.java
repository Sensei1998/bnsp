package bf.bnsp.api.account.dto.form;

import bf.bnsp.api.account.dto.form.partialData.DailyTeamForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramInitForm {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private Optional<Integer> caserneId = Optional.empty();

    private List<DailyTeamForm> equipes = new ArrayList<>();
}
