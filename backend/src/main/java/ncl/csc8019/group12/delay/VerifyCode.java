package ncl.csc8019.group12.delay;

/**
 * @author wei tan
 */
public class VerifyCode extends NormalDelayTask {

    private final String code;
    private int tryTimes;

    public VerifyCode(String email, String code, long ttl) {
        super(email, ttl);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
    }
}
