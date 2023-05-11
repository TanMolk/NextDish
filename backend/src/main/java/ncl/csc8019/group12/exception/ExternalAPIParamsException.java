package ncl.csc8019.group12.exception;

/**
 * This code defines an exception class for API parameters that can be thrown in code without catching.
 *
 * @author wei tan
 */
public final class ExternalAPIParamsException extends ExternalAPIException {
    public ExternalAPIParamsException() {
    }

    public ExternalAPIParamsException(String message) {
        super(message);
    }
}
