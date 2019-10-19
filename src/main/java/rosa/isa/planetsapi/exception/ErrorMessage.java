package rosa.isa.planetsapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    PLANET_NOT_FOUND("Planet not found", HttpStatus.NOT_FOUND),
    PLANET_ALREADY_EXISTS("Planet already exists", HttpStatus.FORBIDDEN),
    DEFAULT_ERROR("An internal occurred", HttpStatus.INTERNAL_SERVER_ERROR);

    private String message;
    private HttpStatus status;

    ErrorMessage(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
