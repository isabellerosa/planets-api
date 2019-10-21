package rosa.isa.planetsapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import rosa.isa.planetsapi.PlanetFixture;
import rosa.isa.planetsapi.exception.CustomError;
import rosa.isa.planetsapi.model.PlanetDTO;
import rosa.isa.planetsapi.service.PlanetService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PlanetControllerTests {
    @Mock
    private PlanetService planetService;
    @InjectMocks
    private PlanetController planetController;

    private PlanetFixture planetFixture;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        planetFixture = new PlanetFixture();
    }

    @Test
    void addPlanet_CorrectPayload_Pass(){
        PlanetDTO payload = new PlanetDTO();
        payload.setName("Earth");
        payload.setQtdMoons(1);
        payload.setType("terrestrial");
        payload.setOrbitalPeriod(365.25);

        PlanetDTO savedPlanet = planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        when(planetService.register(any(PlanetDTO.class))).thenReturn(savedPlanet);

        PlanetDTO returned = planetController.addPlanet(payload);

        Assertions.assertEquals(payload, returned);
        Mockito.verify(planetService, times(1)).register(any());
    }

    @Test
    void addPlanet_EmptyRequiredField_ResponseStatusException(){
        PlanetDTO payload = new PlanetDTO();
        payload.setQtdMoons(1);
        payload.setType("terrestrial");
        payload.setOrbitalPeriod(365.25);

        when(planetService.register(any(PlanetDTO.class))).thenThrow(CustomError.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> planetController.addPlanet(payload));
    }

    @Test
    void getPlanets_WithRequestParams_Pass(){
        List<PlanetDTO> foundPlanets = planetFixture.getPlanetsDtoList();

        when(planetService.findAll(anyInt(), anyInt())).thenReturn(foundPlanets);

        List<PlanetDTO> returnedPlanets = planetController.getPlanets(0, 5);

        Assertions.assertEquals(returnedPlanets.size(), 3);
        Mockito.verify(planetService, times(1)).findAll(anyInt(), anyInt());
    }

    @Test
    void getPlanets_InternalError_ResponseStatusException(){
        when(planetService.findAll(anyInt(), anyInt())).thenThrow(CustomError.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> planetController.getPlanets(0, 10));
    }

    @Test
    void getPlanet_Pass(){
        PlanetDTO foundPlanet =  planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        when(planetService.findByName(anyString())).thenReturn(foundPlanet);

        String planetName = "Earth";
        PlanetDTO returnPlanet = planetController.getPlanet(planetName);

        Assertions.assertEquals(returnPlanet.getName(), planetName);
        verify(planetService, times(1)).findByName(anyString());
    }

    @Test
    void getPlanet_NoPlanetFound_ResponseStatusException(){
        when(planetService.findByName(anyString())).thenThrow(CustomError.class);

        String planetName = "Earth";

        Assertions.assertThrows(ResponseStatusException.class, ()->planetController.getPlanet(planetName));
    }

    @Test
    void updatePlanet_Pass(){
        String planetName = "Ceres";

        PlanetDTO payload = new PlanetDTO();
        payload.setName("Earth");
        payload.setQtdMoons(1);
        payload.setType("terrestrial");
        payload.setOrbitalPeriod(365.25);

        PlanetDTO updatedPlanet = planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        when(planetService.update(anyString(), any(PlanetDTO.class))).thenReturn(updatedPlanet);

        PlanetDTO returned = planetController.updatePlanet(planetName, payload);

        Assertions.assertEquals(payload, returned);
        Mockito.verify(planetService, times(1)).update(anyString(), any(PlanetDTO.class));
    }

    @Test
    void updatePlanet_NoPlanetFound_ResponseStatusException(){
        PlanetDTO payload = new PlanetDTO();
        payload.setName("Earth");
        payload.setQtdMoons(1);
        payload.setType("terrestrial");
        payload.setOrbitalPeriod(365.25);

        String namePlanet = "Ceres";

        when(planetService.update(anyString(), any(PlanetDTO.class))).thenThrow(CustomError.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> planetController.updatePlanet(namePlanet, payload));
    }

    @Test
    void deletePlanet_Pass(){
        String planetName = "Earth";
        PlanetDTO deletedPlanet = planetFixture.getPlanetWithDtoRequiredFieldsFilled();

        when(planetService.remove(anyString())).thenReturn(deletedPlanet);

        PlanetDTO returned = planetController.deletePlanet(planetName);

        Assertions.assertEquals(planetName, returned.getName());
        Mockito.verify(planetService, times(1)).remove(anyString());

    }

    @Test
    void deletePlanet_NoPlanetFound_ResponseStatusException(){
        String planetName = "Earth";

        when(planetService.remove(anyString())).thenThrow(CustomError.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> planetController.deletePlanet(planetName));
    }
}
