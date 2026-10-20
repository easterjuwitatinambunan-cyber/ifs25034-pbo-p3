package domain.exception;

/**
 * Checked exception dasar untuk seluruh hierarki exception domain.
 */
public class DomainException extends Exception {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
