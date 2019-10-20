package rosa.isa.planetsapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rosa.isa.planetsapi.model.PlanetDTO;
import rosa.isa.planetsapi.service.PlanetService;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    private PlanetService planetService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanetController.class);

    @Autowired
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanetDTO addPlanet(@RequestBody PlanetDTO planet)
            throws ResponseStatusException {

        LOGGER.debug(String.format("Registering planet: %s", planet.toString()));

        return planetService.register(planet);
    }

    @GetMapping
    public List<PlanetDTO> getPlanets(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "offset", defaultValue = "1") int offset)
            throws ResponseStatusException{

        LOGGER.debug(String.format("Getting all planets on page %d, displaying %d up to planets per page", page, offset));

        return planetService.findAll(page, offset);
    }

    @GetMapping(value = "/{planet}")
    public PlanetDTO getPlanet(@PathVariable("planet") String planet)
            throws ResponseStatusException {

        LOGGER.debug(String.format("Searching for planet with name: %s", planet));

        return planetService.findByName(planet.replace("_", " "));
    }

    @PutMapping(value = "/{planet}")
    public PlanetDTO updatePlanet(@PathVariable("planet") String planetName, @RequestBody PlanetDTO planet)
            throws ResponseStatusException {

        LOGGER.debug(String.format("Updating planet: %s \n As follows: %s", planetName, planet.toString()));

        return planetService.update(planetName.replace("_", " "), planet);
    }

    @DeleteMapping("/{planet}")
    public PlanetDTO deletePlanet(@PathVariable("planet") String planet)
            throws ResponseStatusException{

        LOGGER.debug(String.format("Deleting planet: %s", planet));

        return planetService.remove(planet.replace("_", " "));
    }


}
