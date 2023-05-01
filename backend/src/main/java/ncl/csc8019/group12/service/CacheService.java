package ncl.csc8019.group12.service;

import ncl.csc8019.group12.controller.UserController;
import ncl.csc8019.group12.delay.NormalDelayTask;
import ncl.csc8019.group12.delay.VerifyCode;
import ncl.csc8019.group12.exception.VerifyTooMuchTimesException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * This Cache Service is to store data in server memory for further use.
 * It helps with the speed of data request and decrease the number of requesting for Google place api.
 * And the cache is stored in a ConcurrentHashMap, which could make concurrency safety.
 *
 * @author wei tan
 */

@Service
public class CacheService {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    {
        File file = new File("./cache_image");

        if (!file.exists()) {
            file.mkdir();
        }

        //verify code expired thread
        new Thread(() -> {
            while (true) {
                try {
                    VerifyCode code = VERIFY_CODE_EXPIRED_QUEUE.take();
                    VERIFY_CODE_MAP.remove(code.getId());

                    log.info("[VerifyCode-Expired] {}", code.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "verify-code-expired-thread").start();

        //try times of verifying code thread
        new Thread(() -> {
            while (true) {
                try {
                    NormalDelayTask task = VERIFY_TIMES_QUEUE.take();
                    VERIFY_CODE_MAP.get(task.getId()).setTryTimes(0);

                    log.info("[VerifyCodeTime-Remove] {}", task.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "remove-try-times-of-verify-code-thread").start();
    }

    /**
     * Key: request params
     * Value: the response
     */
    private final static Map<Integer, JSONObject> RESPONSE_CACHE_STORAGE = new ConcurrentHashMap<>();

    /**
     * Verify expired queue
     */
    private final static DelayQueue<VerifyCode> VERIFY_CODE_EXPIRED_QUEUE = new DelayQueue<>();
    /**
     * Store email and code
     */
    private final static Map<String, VerifyCode> VERIFY_CODE_MAP = new ConcurrentHashMap<>();

    /**
     * Set task to set try time of verifying code to 0
     */
    private final static DelayQueue<NormalDelayTask> VERIFY_TIMES_QUEUE = new DelayQueue<>();

    /**
     * Store response json string to memory
     *
     * @param response response json object
     * @param args     request params
     */
    public void cacheResponse(JSONObject response, Object... args) {
        RESPONSE_CACHE_STORAGE.put(Objects.hash(args), response);
    }

    /**
     * Get cached response json string
     *
     * @param args request params
     * @return null-if it doesn't have cache; else return response json with the same input last returned
     */
    public JSONObject getCachedResponse(Object... args) {
        JSONObject jsonObject = RESPONSE_CACHE_STORAGE.get(Objects.hash(args));
        if (jsonObject == null) {
            return null;
        }
        return new JSONObject(jsonObject.toString());
    }

    public void cachePhoto(String photoReference, byte[] data) {
        File file = new File("./cache_image/" + photoReference + ".jpeg");
        if (!file.exists()) {
            try (
                    FileOutputStream fos = new FileOutputStream(file)
            ) {
                fos.write(data, 0, data.length);
                fos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public byte[] getPhotoCache(String photoReference) {
        File file = new File("./cache_image/" + photoReference + ".jpeg");
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                byte[] data = new byte[in.available()];
                in.read(data);
                return data;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public synchronized void addVerifyCode(VerifyCode code) {
        VERIFY_CODE_MAP.put(code.getId(), code);
        VERIFY_CODE_EXPIRED_QUEUE.add(code);
    }

    public String getVerifyCode(String email, boolean blockTry) {
        VerifyCode verifyCode = VERIFY_CODE_MAP.get(email);

        if (verifyCode == null) {
            return null;
        }

        if (verifyCode.getTryTimes() >= 3 && blockTry) {
            throw new VerifyTooMuchTimesException();
        } else {
            if (verifyCode.getTryTimes() == 0) {
                VERIFY_TIMES_QUEUE.add(new NormalDelayTask(email, 60 * 1000));
            }
            addVerifyTryTime(email);
        }

        return verifyCode.getCode();
    }

    private synchronized void addVerifyTryTime(String email) {
        VerifyCode verifyCode = VERIFY_CODE_MAP.get(email);
        int tryTimes = verifyCode.getTryTimes() + 1;
        verifyCode.setTryTimes(tryTimes);
    }
}
