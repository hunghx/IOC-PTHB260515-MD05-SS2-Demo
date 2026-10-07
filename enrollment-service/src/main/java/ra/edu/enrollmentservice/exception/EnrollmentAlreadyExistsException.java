package ra.edu.enrollmentservice.exception;

public class EnrollmentAlreadyExistsException extends RuntimeException {

    public EnrollmentAlreadyExistsException(String message) {
        super(message);
    }
}
