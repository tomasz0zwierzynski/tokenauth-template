package pl.tomzwi.tokenauth.exception;

public class InvalidUsernamePasswordException extends RuntimeException {
    public InvalidUsernamePasswordException(String message) {
        super(message);
    }

    public InvalidUsernamePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUsernamePasswordException(Throwable cause) {
        super(cause);
    }
}
