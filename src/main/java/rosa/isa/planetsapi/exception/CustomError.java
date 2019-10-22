package rosa.isa.planetsapi.exception;

import org.springframework.web.server.ResponseStatusException;

public class CustomError extends ResponseStatusException {

    public CustomError(ErrorMessage errorMessage) {
        super(errorMessage.getStatus(), errorMessage.getMessage());
    }

    public CustomError(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getStatus(), errorMessage.getMessage(), cause);
    }
}
