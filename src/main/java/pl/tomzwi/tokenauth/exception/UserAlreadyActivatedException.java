package pl.tomzwi.tokenauth.exception;

public class UserAlreadyActivatedException extends RuntimeException {
    public UserAlreadyActivatedException(String message) {
        super(message);
    }

    public UserAlreadyActivatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyActivatedException(Throwable cause) {
        super(cause);
    }
}
