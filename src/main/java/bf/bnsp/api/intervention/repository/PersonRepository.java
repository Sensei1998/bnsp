package bf.bnsp.api.intervention.repository;

import bf.bnsp.api.intervention.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
