package ra.edu.authservice.exception;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
