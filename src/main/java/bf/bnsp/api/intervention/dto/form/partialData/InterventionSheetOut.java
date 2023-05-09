package bf.bnsp.api.intervention.dto.form.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterventionSheetOut {


    private int equipeId;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> presentation = Optional.empty();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> departure = Optional.empty();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> available = Optional.empty();

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Optional<LocalDateTime> checkIn = Optional.empty();

}
