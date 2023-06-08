package bf.bnsp.api.caserne.repository;

import bf.bnsp.api.caserne.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Integer> {
}
