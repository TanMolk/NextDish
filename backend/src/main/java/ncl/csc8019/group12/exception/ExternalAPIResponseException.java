package ncl.csc8019.group12.exception;

/**
 * This code defines an exception class on the API response that can be thrown in code without catching.
 *
 * @author wei tan
 */
public final class ExternalAPIResponseException extends ExternalAPIException {
    public ExternalAPIResponseException(String message) {
        super(String.format("Response is \n %s", message));
    }
}
