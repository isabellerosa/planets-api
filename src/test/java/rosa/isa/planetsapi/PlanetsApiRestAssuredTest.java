package rosa.isa.planetsapi;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rosa.isa.planetsapi.model.PlanetDTO;

import static io.restassured.RestAssured.*;

class PlanetsApiRestAssuredTest {

    @BeforeEach
    void setUp() {
        basePath = "/api";
        port = 8080;
    }

    @Test
    void postPlanet_whenFulfillingRequirements_thenCreated(){
        PlanetDTO planetPayload = new PlanetDTO();
        planetPayload.setName("Test");
        planetPayload.setType("terrestrial");
        planetPayload.setQtdMoons(5);

        given().contentType(ContentType.JSON).body(planetPayload)
                .when().post("/planets")

                .then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    void postPlanet_whenMissingRequiredKey_thenBadRequest(){
        PlanetDTO planetPayload = new PlanetDTO();
        planetPayload.setType("terrestrial");
        planetPayload.setQtdMoons(5);

        given().contentType(ContentType.JSON).body(planetPayload)
                .when().post("/planets")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void postPlanet_whenRegisteringDuplicate_thenConflict(){
        PlanetDTO planetPayload = new PlanetDTO();
        planetPayload.setName("Earth");
        planetPayload.setType("terrestrial");
        planetPayload.setQtdMoons(5);

        given().contentType(ContentType.JSON).body(planetPayload)
                .when().post("/planets")
                .then().statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    void getPlanets_whenPageAndOffsetUndefined_thenOK(){
        Response planets = when().get("/planets");

        Assertions.assertEquals(HttpStatus.SC_OK, planets.getStatusCode());
        Assertions.assertEquals(1, planets.getBody().as(PlanetDTO[].class).length);
    }

    @Test
    void getPlanets_whenPageIs1AndOffsetIs5Undefined_thenOK(){
        int page = 1, offset = 5;
        Response planets = with().queryParam("page", page).queryParam("offset", offset)
                .when().get("/planets");

        Assertions.assertEquals(HttpStatus.SC_OK, planets.getStatusCode());
        Assertions.assertEquals(offset, planets.getBody().as(PlanetDTO[].class).length);
    }

    @Test
    void getPlanet_whenPlanetFound_thenOK(){
        String planet = "Earth";
        Response response = given().pathParam("planet", planet)
                .when().get("/planets/{planet}");

        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assertions.assertEquals(planet, response.getBody().as(PlanetDTO.class).getName());
    }

    @Test
    void getPlanet_whenNoPlanetFound_thenNotFound(){
        String planet = "A_Planet_That_Doesnt_Exist";

        given().pathParam("planet", planet)
                .when().get("/planets/{planet}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void updatePlanet_whenExistingPlanetAndPayloadFulfillingRequirements_thenOK(){
        String planet = "Pluto";
        PlanetDTO payload = new PlanetDTO();
        payload.setName("Test Updated");
        payload.setType("terrestrial");
        payload.setQtdMoons(12);

        Response response = given().pathParam("planet", planet)
                .contentType(ContentType.JSON).body(payload)
                .when().put("/planets/{planet}");

        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assertions.assertEquals(payload.getName(), response.getBody().as(PlanetDTO.class).getName());
    }

    @Test
    void updatePlanet_whenExistingPlanetAndPayloadMissingRequirements_thenBadRequest(){
        String planet = "Haumea";
        PlanetDTO payload = new PlanetDTO();
        payload.setType("terrestrial");
        payload.setQtdMoons(12);

        given().pathParam("planet", planet).contentType(ContentType.JSON).body(payload)
                .when().put("/planets/{planet}")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void updatePlanet_whenPlanetNotFound_thenNotFound(){
        String planet = "A_Planet_That_Doesnt_Exist";
        PlanetDTO payload = new PlanetDTO();
        payload.setName("Test Updated");
        payload.setType("terrestrial");
        payload.setQtdMoons(12);

        given().pathParam("planet", planet).contentType(ContentType.JSON).body(payload)
                .when().put("/planets/{planet}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void deletePlanet_whenExistingPlanet_thenOK(){
        String planet = "Makemake";

        Response response = given().pathParam("planet", planet)
                .when().delete("/planets/{planet}");

        Assertions.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assertions.assertEquals(planet, response.getBody().as(PlanetDTO.class).getName());
    }

    @Test
    void deletePlanet_whenPlanetNotFound_thenNotFound(){
        String planet = "A_Planet_That_Doesnt_Exist";

        given().pathParam("planet", planet)
                .when().delete("/planets/{planet}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

}
