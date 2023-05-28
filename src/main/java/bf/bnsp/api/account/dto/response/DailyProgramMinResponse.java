package bf.bnsp.api.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramMinResponse {

    private long id;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private int casernceId;

    private String caserneName;

    private String caserneCity;

    private String caserneArea;

    private Optional<FonctionTeamResponse> caporalTeam;

    private Optional<FonctionTeamResponse> sergentTeam;
}
