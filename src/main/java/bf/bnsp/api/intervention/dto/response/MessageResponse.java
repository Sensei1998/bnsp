package bf.bnsp.api.intervention.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@Setter
public class MessageResponse {

    private int id;

    private int interventionId;

    private int caserneId;

    private long equipeId;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime sentAt;

    private String message;
}
