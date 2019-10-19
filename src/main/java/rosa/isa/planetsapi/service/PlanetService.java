package rosa.isa.planetsapi.service;

import rosa.isa.planetsapi.model.PlanetDTO;

import java.util.List;

public interface PlanetService {
    PlanetDTO register(PlanetDTO planet) throws Exception;
    PlanetDTO update(String planetName, PlanetDTO planet) throws Exception;
    List<PlanetDTO> findAll(int page, int size);
    PlanetDTO findByName(String name) throws Exception;
    void remove(String planet);
}
