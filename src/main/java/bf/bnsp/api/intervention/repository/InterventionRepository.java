package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Berickal
 */
public interface InterventionRepository extends JpaRepository<Intervention, Integer> {

    @Transactional
    long countByDateBetweenAndHiddenFalse(LocalDateTime startTime, LocalDateTime endTime);

    @Transactional
    long countByStatusAndHiddenFalse(String status);

    @Transactional
    long countByStatusAndDateBetweenAndHiddenFalse(String status, LocalDateTime startTime, LocalDateTime endTime);

}
