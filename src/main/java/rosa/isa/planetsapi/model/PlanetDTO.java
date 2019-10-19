package rosa.isa.planetsapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlanetDTO {
    @JsonProperty(value = "planet", required = true)
    private String name;

    private String type;

    private double radius;

    @JsonProperty("moons")
    private int qtdMoons;

    @JsonProperty("rotation_period")
    private double rotationPeriod;

    @JsonProperty("orbital_period")
    private double orbitalPeriod;
}
