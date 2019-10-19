package rosa.isa.planetsapi.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rosa.isa.planetsapi.model.Planet;
import rosa.isa.planetsapi.model.PlanetDTO;
import rosa.isa.planetsapi.repository.PlanetRepository;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService {
    private PlanetRepository planetRepository;

    @Autowired
    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public PlanetDTO register(PlanetDTO planetDTO) throws Exception {
        ModelMapper mapper = new ModelMapper();
        Planet planet = mapper.map(planetDTO, Planet.class);

        if(planetRepository.findByName(planet.getName()) != null){
            throw new Exception("Planet already registered!");
        }

        try{
            planet = planetRepository.save(planet);
        }catch (Exception e){
            throw new Exception("An error occurred when trying to register planet");
        }

        return mapper.map(planet, PlanetDTO.class);
    }


    @Override
    public PlanetDTO update(String planetName, PlanetDTO planetDTO) throws Exception {
        ModelMapper mapper = new ModelMapper();
        Planet stalePlanet = planetRepository.findByName(planetName);

        if (stalePlanet == null ){
            throw new Exception("No planet "+ planetName + " registered");
        }

        mapper.map(planetDTO, stalePlanet);

        Planet updated = planetRepository.save(stalePlanet);

        return mapper.map(updated, PlanetDTO.class);
    }

    @Override
    public List<PlanetDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(
                (page <= 0) ? 0 : page,
                (size <= 0) ? 1 : size);
        Page<Planet> planets = planetRepository.findAll(pageable);

        return new ModelMapper().map(planets.getContent(), new TypeToken<List<PlanetDTO>>(){}.getType());
    }

    @Override
    public PlanetDTO findByName(String name) throws Exception {
        Planet planet = planetRepository.findByName(name);

        if(planet == null){
            throw new Exception("No planet " + name + " registered");
        }

        return new ModelMapper().map(planet, PlanetDTO.class);
    }

    @Override
    public void remove(String planet) {
        Planet planetFound = planetRepository.findByName(planet);

        if(planetFound != null){
            planetRepository.delete(planetFound);
        }

    }
}
