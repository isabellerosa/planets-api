package rosa.isa.planetsapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PlanetDTO {
    @JsonProperty(value = "planet", required = true)
    private String name;

    private String type;

    private Double radius;

    @JsonProperty("moons")
    private Integer qtdMoons;

    @JsonProperty("rotation_period")
    private Double rotationPeriod;

    @JsonProperty(value = "orbital_period")
    private Double orbitalPeriod;
}
