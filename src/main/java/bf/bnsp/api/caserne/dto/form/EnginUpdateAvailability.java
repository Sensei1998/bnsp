package bf.bnsp.api.caserne.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Berickal
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnginUpdateAvailability {

    private int enginId;

    private boolean available;
}
