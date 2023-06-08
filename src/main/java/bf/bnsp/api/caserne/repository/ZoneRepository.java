package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Integer> {

    @Transactional
    List<Zone> findByOrderByZoneAsc();

}
