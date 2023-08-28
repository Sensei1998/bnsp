package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("SELECT COUNT(*) FROM Intervention int WHERE int.incident.category = ?1")
    long countByIncidentCategory(String category);

    @Query("SELECT COUNT(*) FROM Intervention int WHERE int.incident.category = ?1 AND int.date >= ?2 AND int.date <= ?3")
    long countByIncidentCategoryAndDateBetween(String category, LocalDateTime startTime, LocalDateTime endTime);

}
