package rosa.isa.planetsapi.service;

import rosa.isa.planetsapi.model.PlanetDTO;

import java.util.List;

public interface PlanetService {
    PlanetDTO register(PlanetDTO planet);

    PlanetDTO update(String planetName, PlanetDTO planet);

    List<PlanetDTO> findAll(int page, int size);

    PlanetDTO findByName(String name);

    void remove(String planet);
}
