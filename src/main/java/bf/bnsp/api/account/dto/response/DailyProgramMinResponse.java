package bf.bnsp.api.account.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyProgramMinResponse {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private int casernceId;

    private String caserneName;

    private String caserneCity;

    private String caserneArea;
}
