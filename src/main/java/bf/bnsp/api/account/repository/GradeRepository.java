package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Berickal
 */
public interface GradeRepository extends JpaRepository<Grade, Integer> {
}
