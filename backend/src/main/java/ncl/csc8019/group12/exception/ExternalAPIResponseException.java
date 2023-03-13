package ncl.csc8019.group12.exception;

/**
 * @author wei tan
 */
public final class ExternalAPIResponseException extends ExternalAPIException {
    public ExternalAPIResponseException(String message) {
        super(String.format("Response is \n %s", message));
    }
}
