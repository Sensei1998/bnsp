package bf.bnsp.api.account.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramUpdateForm {

    private int dailyProgramId;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date = LocalDate.now();

    private int teamId;

    private List<DailyProgramTeamForm> teamProgramList = new ArrayList<>();

}
