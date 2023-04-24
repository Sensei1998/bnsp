package bf.bnsp.api.intervention.dto.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Berickal
 */
@Data
@NoArgsConstructor
public class InterventionInitForm {

    private int cctoId;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime time;

    private String provenance;

    private String phoneNumber;

    private String name;

    private String address;

    private float longitude;

    private float latitude;

    private String precision;
}
