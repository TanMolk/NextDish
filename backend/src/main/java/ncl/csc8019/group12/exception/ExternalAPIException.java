package ncl.csc8019.group12.exception;

/**
 * This code defines an exception class on the API that can be thrown in code without catching.
 *
 * @author wei tan
 */
public class ExternalAPIException extends RuntimeException {
    public ExternalAPIException() {
    }

    public ExternalAPIException(String message) {
        super(message);
    }
}
