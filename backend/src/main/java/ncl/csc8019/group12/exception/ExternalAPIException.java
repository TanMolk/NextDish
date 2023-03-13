package ncl.csc8019.group12.exception;

/**
 * @author wei tan
 */
public class ExternalAPIException extends RuntimeException {
    public ExternalAPIException() {
    }

    public ExternalAPIException(String message) {
        super(message);
    }
}
