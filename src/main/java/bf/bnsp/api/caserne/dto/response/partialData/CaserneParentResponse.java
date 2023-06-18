package bf.bnsp.api.caserne.dto.response.partialData;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class CaserneParentResponse {

    private int id;

    private String name;
}
