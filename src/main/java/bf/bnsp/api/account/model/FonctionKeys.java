package bf.bnsp.api.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FonctionKeys implements Serializable {

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @ManyToOne
    private Agent agent;
}
