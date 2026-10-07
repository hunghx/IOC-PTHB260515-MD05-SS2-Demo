package ra.edu.authservice.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final int statusCode;

    public AppException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public AppException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
