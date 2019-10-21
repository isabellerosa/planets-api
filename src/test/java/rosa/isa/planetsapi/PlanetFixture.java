package rosa.isa.planetsapi;

import lombok.Getter;
import rosa.isa.planetsapi.model.Planet;
import rosa.isa.planetsapi.model.PlanetDTO;

import java.util.Arrays;
import java.util.List;

@Getter
public class PlanetFixture {

    private final Planet planetWithRequiredFieldsFilled;
    private final Planet planetWithRequiredFieldsMissing;

    private final PlanetDTO planetWithDtoRequiredFieldsFilled;
    private final PlanetDTO planetWithDtoRequiredFieldsMissing;

    private final List<Planet> planetsList;
    private final List<PlanetDTO> planetsDtoList;

    public PlanetFixture(){
        planetWithRequiredFieldsFilled = new Planet();
        planetWithRequiredFieldsFilled.setId(3L);
        planetWithRequiredFieldsFilled.setName("Earth");
        planetWithRequiredFieldsFilled.setQtdMoons(1);
        planetWithRequiredFieldsFilled.setType("terrestrial");
        planetWithRequiredFieldsFilled.setOrbitalPeriod(365.25);

        planetWithRequiredFieldsMissing = new Planet();
        planetWithRequiredFieldsMissing.setId(3L);
        planetWithRequiredFieldsMissing.setQtdMoons(1);
        planetWithRequiredFieldsMissing.setType("terrestrial");
        planetWithRequiredFieldsMissing.setOrbitalPeriod(365.25);

        planetWithDtoRequiredFieldsFilled = new PlanetDTO();
        planetWithDtoRequiredFieldsFilled.setName("Earth");
        planetWithDtoRequiredFieldsFilled.setQtdMoons(1);
        planetWithDtoRequiredFieldsFilled.setType("terrestrial");
        planetWithDtoRequiredFieldsFilled.setOrbitalPeriod(365.25);

        planetWithDtoRequiredFieldsMissing = new PlanetDTO();
        planetWithDtoRequiredFieldsMissing.setQtdMoons(1);
        planetWithDtoRequiredFieldsMissing.setType("terrestrial");
        planetWithDtoRequiredFieldsMissing.setOrbitalPeriod(365.25);


        Planet planet2 = new Planet();
        planet2.setId(4L);
        planet2.setName("Mercury");
        planet2.setQtdMoons(0);

        Planet planet3 = new Planet();
        planet3.setId(7L);
        planet3.setName("Venus");
        planet3.setQtdMoons(0);

        PlanetDTO planetDTO2 = new PlanetDTO();
        planetDTO2.setName("Mercury");
        planetDTO2.setQtdMoons(0);

        PlanetDTO planetDTO3 = new PlanetDTO();
        planetDTO3.setName("Venus");
        planetDTO3.setQtdMoons(0);


        planetsList = Arrays.asList(planetWithRequiredFieldsFilled, planet2, planet3);
        planetsDtoList = Arrays.asList(planetWithDtoRequiredFieldsFilled, planetDTO2, planetDTO3);

    }
}
