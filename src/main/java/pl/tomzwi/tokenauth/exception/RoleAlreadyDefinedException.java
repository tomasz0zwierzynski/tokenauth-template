package pl.tomzwi.tokenauth.exception;

public class RoleAlreadyDefinedException extends RuntimeException {
    public RoleAlreadyDefinedException(String message) {
        super(message);
    }

    public RoleAlreadyDefinedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleAlreadyDefinedException(Throwable cause) {
        super(cause);
    }
}
