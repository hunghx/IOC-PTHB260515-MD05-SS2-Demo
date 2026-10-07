package ra.edu.authservice.exception;

public class EmailAlreadyExistsException extends AppException {

    public EmailAlreadyExistsException(String email) {
        super("Email đã tồn tại trong hệ thống: " + email, 409);
    }
}
