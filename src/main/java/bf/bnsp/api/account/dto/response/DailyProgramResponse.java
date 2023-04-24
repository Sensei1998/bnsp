package bf.bnsp.api.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
public class DailyProgramResponse {

    private long id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private int casernceId;

    private String caserneName;

    private String caserneCity;

    private String caserneArea;

    private List<FonctionTeamResponse> teams;
}
