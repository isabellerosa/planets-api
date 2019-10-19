package rosa.isa.planetsapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rosa.isa.planetsapi.model.PlanetDTO;
import rosa.isa.planetsapi.service.PlanetService;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    private PlanetService planetService;

    @Autowired
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    public PlanetDTO addPlanet(@RequestBody PlanetDTO planet) throws Exception {
        return planetService.register(planet);
    }

    @GetMapping
    public List<PlanetDTO> getPlanets(@RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "offset", defaultValue = "1") int offset){
        return planetService.findAll(page, offset);
    }

    @GetMapping(value = "/{planet}")
    public PlanetDTO getPlanet(@PathVariable("planet") String planet) throws Exception {
        return planetService.findByName(planet.replace("_", " "));
    }

    @PutMapping(value = "/{planet}")
    public PlanetDTO updatePlanet(@PathVariable("planet") String planetName, @RequestBody PlanetDTO planet) throws Exception {
        return planetService.update(planetName.replace("_", " "), planet);
    }

    @DeleteMapping
    public void deletePlanet(String planet){
        planetService.remove(planet);
    }
}
