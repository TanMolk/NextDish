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
 * @author Wei tan & Pulei & Rachel
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
    /**
     * The cachePhoto method caches an array of bytes of images in a specified folder.
     * so that the next time it is fetched it can be read directly from the file.
     * avoiding repeated requests for images from the network.
     *
     * @param  photoReference It is the identifier of the image in the Google Places API
     * @param  data           The image itself is represented as a byte array of the type "data"
     * @return                void(The cache function does not require a return value)
     */
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

    /**
     * The getPhotoCache method get the relevant images in the cache with the photoReference keyword.
     * Reduced frequency of Google API usage and improved system efficiency
     *
     * @param  photoReference It is the identifier of the image in the Google Places API
     * @return                If the relevant image exists,return An array of bytes corresponding to the image
     *                        If the relevant image doesn't exist,return null
     */
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

    /**
     * This code is a synchronous method for adding a new CAPTCHA object to a Map that stores CAPTCHA objects.
     * And adds the same CAPTCHA object to a queue that stores expired CAPTCHA objects.
     * @param  code       It is the identifier of the image in the Google Places API
     * @return            Data storage function, no return value required
     */
    public synchronized void addVerifyCode(VerifyCode code) {
        VERIFY_CODE_MAP.put(code.getId(), code);
        VERIFY_CODE_EXPIRED_QUEUE.add(code);
    }

    /**
     * The purpose of this code is to get a user's CAPTCHA in Cache.
     * If the user has more than three attempts and blockTry is true then a VerifyTooMuchTimesException will be thrown,
     * otherwise the number of attempts will be increased and the last user's CAPTCHA will be returned.
     * If the user has 0 attempts before the code is obtained,
     * a task is also added to VERIFY_TIMES_QUEUE indicating that the user will need 60 seconds to try to obtain the code again,
     * A 60-second cache prevents users from continuously requesting CAPTCHA and thus affecting server performance.
     * @param  email       email of user
     * @param  blockTry    Lock function for turning on or off the limit of up to 3 captcha requests
     * @return             -null(There is no authentication code for this user in the cache)
     *                     -otherwise will return the VerifyCode by user in Cache
     */
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


    /**
     * This code is to increase the number of user authentication verification code attempts once.
     *
     * @param  email   This user's email
     * @return         void
     */
    private synchronized void addVerifyTryTime(String email) {
        VerifyCode verifyCode = VERIFY_CODE_MAP.get(email);
        int tryTimes = verifyCode.getTryTimes() + 1;
        verifyCode.setTryTimes(tryTimes);
    }
}
