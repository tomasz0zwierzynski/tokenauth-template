package pl.tomzwi.tokenauth.exception;

public class UserActivateCodeNotCorrectException extends RuntimeException {
    public UserActivateCodeNotCorrectException(String message) {
        super(message);
    }

    public UserActivateCodeNotCorrectException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserActivateCodeNotCorrectException(Throwable cause) {
        super(cause);
    }
}
