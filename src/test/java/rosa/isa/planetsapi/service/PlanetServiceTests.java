package rosa.isa.planetsapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import rosa.isa.planetsapi.PlanetFixture;
import rosa.isa.planetsapi.exception.CustomError;
import rosa.isa.planetsapi.model.Planet;
import rosa.isa.planetsapi.model.PlanetDTO;
import rosa.isa.planetsapi.repository.PlanetRepository;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class PlanetServiceTests {
    @Mock
    private PlanetRepository planetRepository;
    @InjectMocks
    private PlanetServiceImpl planetService;

    private static PlanetFixture planetFixture;

    @BeforeAll
    static void init(){
        planetFixture = new PlanetFixture();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void register_Pass(){
        Planet returnedPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(null);
        Mockito.when(planetRepository.save(any(Planet.class))).thenReturn(returnedPlanet);

        PlanetDTO newPlanet = planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        PlanetDTO savedPlanet = planetService.register(newPlanet);

        Assertions.assertEquals(savedPlanet.getName(), newPlanet.getName());

        Mockito.verify(planetRepository, Mockito.times(1)).save(any());
    }

    @Test
    void register_PlanetAlreadyRegistered_CustomErrorException(){
        Planet returnedPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(returnedPlanet);

        PlanetDTO newPlanet = planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        Assertions.assertThrows(CustomError.class, () -> planetService.register(newPlanet));
        Mockito.verify(planetRepository, Mockito.never()).save(any());
    }

    @Test
    void register_MissingRequiredFields_CustomErrorException(){
        Mockito.when(planetRepository.findByName(anyString())).thenReturn(null);
        Mockito.when(planetRepository.save(any(Planet.class))).thenThrow(ConstraintViolationException.class);

        PlanetDTO newPlanet = planetFixture.getPlanetWithDtoRequiredFieldsMissing();

        Assertions.assertThrows(CustomError.class, () -> planetService.register(newPlanet));
        Mockito.verify(planetRepository, Mockito.times(1)).save(any(Planet.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void findAll_Pagination_Pass(){
        Page<Planet> returnedPage = Mockito.mock(Page.class);

        Mockito.when(planetRepository.findAll(any(Pageable.class))).thenReturn(returnedPage);

        List<PlanetDTO> returnedPlanets = planetService.findAll(0, 3);

        Assertions.assertNotNull(returnedPlanets);

        Mockito.verify(planetRepository, Mockito.times(1)).findAll(any(Pageable.class));
        Mockito.verify(planetRepository, Mockito.never()).findAll(any(Sort.class));
    }

    @Test
    void findAll_Pagination_CustomErrorException(){
        Mockito.when(planetRepository.findAll(Mockito.any(Pageable.class))).thenThrow(RuntimeException.class);

        Assertions.assertThrows(CustomError.class, () -> planetService.findAll(0, 4));
    }

    @Test
    void findByName_Pass(){
        Planet returnedPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(returnedPlanet);

        String planetName = "Earth";
        PlanetDTO foundPlanet = planetService.findByName(planetName);

        Assertions.assertSame(foundPlanet.getClass(), PlanetDTO.class);
        Assertions.assertEquals(planetName, foundPlanet.getName());

        Mockito.verify(planetRepository, Mockito.times(1)).findByName(anyString());
    }

    @Test
    void findByName_NoPlanetFound_CustomErrorException(){
        Mockito.when(planetRepository.findByName(anyString())).thenReturn(null);

        Assertions.assertThrows(CustomError.class, () -> planetService.findByName("Earth"));
    }

    @Test
    void update_Pass(){
        String newName = "Ceres";
        Planet foundPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();
        Planet updatedPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();
        updatedPlanet.setName(newName);

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(foundPlanet);
        Mockito.when(planetRepository.save(any(Planet.class))).thenReturn(updatedPlanet);

        PlanetDTO updatedPlanetDto = planetFixture.getPlanetWithDtoRequiredFieldsFilled();
        updatedPlanetDto.setName(newName);
        PlanetDTO returned = planetService.update("Earth", updatedPlanetDto);

        Assertions.assertEquals(returned.getName(), newName);
        Mockito.verify(planetRepository, Mockito.times(1)).save(any());
    }

    @Test
    void update_PlanetNotFound_CustomErrorException(){
        String newName = "Ceres";
        PlanetDTO updatedPlanetDto = planetFixture.getPlanetWithDtoRequiredFieldsFilled();
        updatedPlanetDto.setName(newName);

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(null);

        Assertions.assertThrows(CustomError.class, () -> planetService.update("Earth", updatedPlanetDto));
        Mockito.verify(planetRepository, Mockito.never()).save(any());
    }

    @Test
    void update_CustomErrorException(){
        String oldName = "Venus";
        String newName = "Ceres";
        Planet foundPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();
        PlanetDTO updatedPlanetDto = planetFixture.getPlanetWithDtoRequiredFieldsFilled();
        updatedPlanetDto.setName(newName);

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(foundPlanet);
        Mockito.when(planetRepository.save(any(Planet.class))).thenReturn(null);

        Assertions.assertThrows(CustomError.class, () -> planetService.update(oldName, updatedPlanetDto));
        Mockito.verify(planetRepository, Mockito.atMostOnce()).save(any());
    }

    @Test
    void remove_Pass(){
        Planet planet = planetFixture.getPlanetWithRequiredFieldsFilled();

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(planet);
        Mockito.doNothing().when(planetRepository).delete(any(Planet.class));

        String planetName = "Earth";
        PlanetDTO deletedPlanet = planetService.remove(planetName);

        Assertions.assertSame(deletedPlanet.getClass(), PlanetDTO.class);
        Assertions.assertEquals(deletedPlanet.getName(), planetName);

        Mockito.verify(planetRepository, Mockito.times(1)).delete(any(Planet.class));
    }

    @Test
    void remove_NoPlanetFound_CustomErrorException(){
        Mockito.when(planetRepository.findByName(anyString())).thenReturn(null);

        String planetName = "Earth";

        Assertions.assertThrows(CustomError.class, () -> planetService.remove(planetName));

        Mockito.verify(planetRepository, Mockito.never()).delete(any(Planet.class));
    }

    @Test
    void remove_CustomErrorException(){
        Planet planet = planetFixture.getPlanetWithRequiredFieldsFilled();

        Mockito.when(planetRepository.findByName(anyString())).thenReturn(planet);
        Mockito.doThrow(RuntimeException.class).when(planetRepository).delete(any(Planet.class));

        String planetName = "Earth";

        Assertions.assertThrows(CustomError.class, () -> planetService.remove(planetName));
    }
}
