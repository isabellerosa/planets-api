package rosa.isa.planetsapi.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        when(planetRepository.findByName(anyString())).thenReturn(null);
        when(planetRepository.save(any(Planet.class))).thenReturn(returnedPlanet);

        PlanetDTO newPlanet = planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        PlanetDTO savedPlanet = planetService.register(newPlanet);

        assertEquals(savedPlanet.getName(), newPlanet.getName());

        verify(planetRepository, times(1)).save(any());
    }

    @Test
    void register_PlanetAlreadyRegistered_CustomErrorException(){
        Planet returnedPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();

        when(planetRepository.findByName(anyString())).thenReturn(returnedPlanet);

        PlanetDTO newPlanet = planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        assertThrows(CustomError.class, () -> planetService.register(newPlanet));
        verify(planetRepository, never()).save(any());
    }

    @Test
    void register_MissingRequiredFields_CustomErrorException(){
        when(planetRepository.findByName(anyString())).thenReturn(null);
        when(planetRepository.save(any(Planet.class))).thenThrow(ConstraintViolationException.class);

        PlanetDTO newPlanet = planetFixture.getPlanetWithDtoRequiredFieldsMissing();

        assertThrows(CustomError.class, () -> planetService.register(newPlanet));
        verify(planetRepository, times(1)).save(any(Planet.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void findAll_Pagination_Pass(){
        Page<Planet> returnedPage = mock(Page.class);

        when(planetRepository.findAll(any(Pageable.class))).thenReturn(returnedPage);

        List<PlanetDTO> returnedPlanets = planetService.findAll(0, 3);

        assertNotNull(returnedPlanets);

        verify(planetRepository, times(1)).findAll(any(Pageable.class));
        verify(planetRepository, never()).findAll(any(Sort.class));
    }

    @Test
    void findAll_Pagination_CustomErrorException(){
        when(planetRepository.findAll(any(Pageable.class))).thenThrow(RuntimeException.class);

        assertThrows(CustomError.class, () -> planetService.findAll(0, 4));
    }

    @Test
    void findByName_Pass(){
        Planet returnedPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();

        when(planetRepository.findByName(anyString())).thenReturn(returnedPlanet);

        String planetName = "Earth";
        PlanetDTO foundPlanet = planetService.findByName(planetName);

        assertSame(PlanetDTO.class, foundPlanet.getClass());
        assertEquals(planetName, foundPlanet.getName());

        verify(planetRepository, times(1)).findByName(anyString());
    }

    @Test
    void findByName_NoPlanetFound_CustomErrorException(){
        when(planetRepository.findByName(anyString())).thenReturn(null);

        assertThrows(CustomError.class, () -> planetService.findByName("Earth"));
    }

    @Test
    void update_Pass(){
        String newName = "Ceres";
        Planet foundPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();
        Planet updatedPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();
        updatedPlanet.setName(newName);

        when(planetRepository.findByName(anyString())).thenReturn(foundPlanet);
        when(planetRepository.save(any(Planet.class))).thenReturn(updatedPlanet);

        PlanetDTO updatedPlanetDto = planetFixture.getPlanetWithDtoRequiredFieldsFilled();
        updatedPlanetDto.setName(newName);
        PlanetDTO returned = planetService.update("Earth", updatedPlanetDto);

        assertEquals(newName, returned.getName());
        verify(planetRepository, times(1)).save(any());
    }

    @Test
    void update_PlanetNotFound_CustomErrorException(){
        String newName = "Ceres";
        PlanetDTO updatedPlanetDto = planetFixture.getPlanetWithDtoRequiredFieldsFilled();
        updatedPlanetDto.setName(newName);

        when(planetRepository.findByName(anyString())).thenReturn(null);

        assertThrows(CustomError.class, () -> planetService.update("Earth", updatedPlanetDto));
        verify(planetRepository, never()).save(any());
    }

    @Test
    void update_CustomErrorException(){
        String newName = "Ceres";
        Planet foundPlanet = planetFixture.getPlanetWithRequiredFieldsFilled();
        PlanetDTO updatedPlanetDto = planetFixture.getPlanetWithDtoRequiredFieldsFilled();
        updatedPlanetDto.setName(newName);

        when(planetRepository.findByName(anyString())).thenReturn(foundPlanet);
        when(planetRepository.save(any(Planet.class))).thenReturn(null);

        assertThrows(CustomError.class, () -> planetService.update(foundPlanet.getName(), updatedPlanetDto));
        verify(planetRepository, atMostOnce()).save(any());
    }

    @Test
    void remove_Pass(){
        Planet planet = planetFixture.getPlanetWithRequiredFieldsFilled();

        when(planetRepository.findByName(anyString())).thenReturn(planet);
        doNothing().when(planetRepository).delete(any(Planet.class));

        String planetName = planet.getName();
        PlanetDTO deletedPlanet = planetService.remove(planetName);

        assertSame(PlanetDTO.class, deletedPlanet.getClass());
        assertEquals(planetName, deletedPlanet.getName());

        verify(planetRepository, times(1)).delete(any(Planet.class));
    }

    @Test
    void remove_NoPlanetFound_CustomErrorException(){
        when(planetRepository.findByName(anyString())).thenReturn(null);

        String planetName = "Earth";

        assertThrows(CustomError.class, () -> planetService.remove(planetName));

        verify(planetRepository, never()).delete(any(Planet.class));
    }

    @Test
    void remove_CustomErrorException(){
        Planet planet = planetFixture.getPlanetWithRequiredFieldsFilled();

        when(planetRepository.findByName(anyString())).thenReturn(planet);
        doThrow(RuntimeException.class).when(planetRepository).delete(any(Planet.class));

        String planetName = "Earth";

        assertThrows(CustomError.class, () -> planetService.remove(planetName));
    }
}
