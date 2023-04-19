package bf.bnsp.api.account.dto.response.partialData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class EnginInfo {

    private int enginId;

    private String immatriculation;

    private String enginType;

    private String description;

    private boolean sortie;

    private boolean available;
}
