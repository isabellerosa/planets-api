package rosa.isa.planetsapi.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rosa.isa.planetsapi.model.Planet;

@Repository
public interface PlanetRepository extends PagingAndSortingRepository<Planet, Long> {
    Planet findByName(String name);
}
