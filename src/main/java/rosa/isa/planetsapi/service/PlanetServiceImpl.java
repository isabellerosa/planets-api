package rosa.isa.planetsapi.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rosa.isa.planetsapi.exception.CustomError;
import rosa.isa.planetsapi.exception.ErrorMessage;
import rosa.isa.planetsapi.model.Planet;
import rosa.isa.planetsapi.model.PlanetDTO;
import rosa.isa.planetsapi.repository.PlanetRepository;

import java.util.List;

@Service
public class PlanetServiceImpl implements PlanetService {
    private PlanetRepository planetRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetServiceImpl.class);

    @Autowired
    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public PlanetDTO register(PlanetDTO planetDTO) throws CustomError {
        ModelMapper mapper = new ModelMapper();
        Planet planet = mapper.map(planetDTO, Planet.class);

        try{
            if(planetRepository.findByName(planet.getName()) != null){
                throw new CustomError(ErrorMessage.PLANET_ALREADY_EXISTS);
            }

            planet = planetRepository.save(planet);

            return mapper.map(planet, PlanetDTO.class);
        }catch (CustomError customError){
            LOGGER.error(String.format("Caught CustomError with message: %s", customError.getMessage()));
            throw customError;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new CustomError(ErrorMessage.DEFAULT_ERROR, e);
        }
    }


    @Override
    public PlanetDTO update(String planetName, PlanetDTO planetDTO) throws CustomError {
        try {
            Planet stalePlanet = planetRepository.findByName(planetName);

            if (stalePlanet == null) {
                throw new CustomError(ErrorMessage.PLANET_NOT_FOUND);
            }

            ModelMapper mapper = new ModelMapper();
            mapper.map(planetDTO, stalePlanet);

            Planet updated = planetRepository.save(stalePlanet);

            return mapper.map(updated, PlanetDTO.class);

        }catch (CustomError customError){
            LOGGER.error(String.format("Caught CustomError with message: %s", customError.getMessage()));
            throw customError;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new CustomError(ErrorMessage.DEFAULT_ERROR, e);
        }
    }


    @Override
    public List<PlanetDTO> findAll(int page, int size) throws CustomError {
        Pageable pageable = PageRequest.of(
                Math.max(0, page),
                Math.max(5, size));

        try {
            Page<Planet> planets = planetRepository.findAll(pageable);

            return new ModelMapper().map(planets.getContent(), new TypeToken<List<PlanetDTO>>() {}.getType());
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new CustomError(ErrorMessage.DEFAULT_ERROR, e);
        }
    }

    @Override
    public PlanetDTO findByName(String name) throws CustomError {
        try {
            Planet planet = planetRepository.findByName(name);

            if(planet == null){
                throw new CustomError(ErrorMessage.PLANET_NOT_FOUND);
            }

            return new ModelMapper().map(planet, PlanetDTO.class);
        }catch (CustomError customError){
            LOGGER.error(String.format("Caught CustomError with message: %s", customError.getMessage()));
            throw customError;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new CustomError(ErrorMessage.DEFAULT_ERROR, e);
        }
    }


    @Override
    public PlanetDTO remove(String planet) throws CustomError {

        try {
            Planet planetFound = planetRepository.findByName(planet);

            if (planetFound == null) {
                throw new CustomError(ErrorMessage.PLANET_NOT_FOUND);
            }

            planetRepository.delete(planetFound);

            return new ModelMapper().map(planetFound, PlanetDTO.class);
        }catch (CustomError customError){
            LOGGER.error(String.format("Caught CustomError with message: %s", customError.getMessage()));
            throw customError;
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new CustomError(ErrorMessage.DEFAULT_ERROR, e);
        }
    }
}
