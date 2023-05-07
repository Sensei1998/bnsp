package bf.bnsp.api.intervention.dto.response;

import bf.bnsp.api.caserne.model.Caserne;
import bf.bnsp.api.intervention.dto.form.partialData.InterventionIncidentInfo;
import bf.bnsp.api.intervention.dto.response.partialData.InterventionCaserneData;
import bf.bnsp.api.intervention.model.additional.Incident;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@Setter
public class InterventionResponse {

    private int interventionId;

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

    private Incident incident;

    private String status;

    private List<InterventionCaserneData> casernes = new ArrayList<>();
}
