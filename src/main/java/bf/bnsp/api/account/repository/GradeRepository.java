package bf.bnsp.api.account.repository;

import bf.bnsp.api.account.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
}
