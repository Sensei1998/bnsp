package bf.bnsp.api.intervention.dto.form;

import bf.bnsp.api.intervention.dto.form.partialData.InterventionCallerInfo;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionCaserne;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionIncidentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterventionInitAdvancedForm {
    private int cctoId;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime time;

    private InterventionCallerInfo appelant;

    private InterventionIncidentInfo incident;

    private List<InterventionCaserne> casernes = new ArrayList<>();
}
