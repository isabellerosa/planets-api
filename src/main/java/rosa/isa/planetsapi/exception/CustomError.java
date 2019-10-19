package rosa.isa.planetsapi.exception;

import lombok.Data;
import org.springframework.web.server.ResponseStatusException;

@Data
public class CustomError extends ResponseStatusException {
    private ErrorMessage errorMessage;

    public CustomError(ErrorMessage errorMessage) {
        super(errorMessage.getStatus(), errorMessage.getMessage());
    }

    public CustomError(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getStatus(), errorMessage.getMessage(), cause);
    }
}
