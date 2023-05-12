package bf.bnsp.api.intervention.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@Setter
public class InterventionTeamData {

    private long teamId;

    private String type;

    private Optional<InterventionTeamEnginData> engin = Optional.empty();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> presentation = Optional.empty();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> departure = Optional.empty();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> available = Optional.empty();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> checkIn = Optional.empty();

    private List<InterventionTeamMemberData> members = new ArrayList<>();
}
