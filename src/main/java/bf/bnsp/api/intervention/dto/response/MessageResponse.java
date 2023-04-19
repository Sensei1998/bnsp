package bf.bnsp.api.intervention.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Setter
public class MessageResponse {

    private int id;

    private int interventionId;

    private int caserneId;

    private int equipeId;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sentAt;

    private String message;
}
