package ncl.csc8019.group12.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This Cache Service is to store data in server memory for further use.
 * It helps with the speed of data request and decrease the number of requesting for Google place api.
 * And the cache is stored in a ConcurrentHashMap, which could make concurrency safety.
 *
 * @author wei tan
 */

@Service
public class CacheService {
    {
        File file = new File("./cache_image");

        if (!file.exists()){
            file.mkdir();
        }
    }

    /**
     * Key: request params
     * Value: the response
     */
    private final static Map<Integer, JSONObject> RESPONSE_CACHE_STORAGE = new ConcurrentHashMap<>();

    /**
     * Store response json string to memory
     *
     * @param response response json object
     * @param args               request params
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
        return RESPONSE_CACHE_STORAGE.get(Objects.hash(args));
    }

    public void cachePhoto(String photoReference, byte[] data) {
        File file = new File("./cache_image/" + photoReference + ".jpeg");
        if (!file.exists()) {
            try (
                    FileOutputStream fos = new FileOutputStream(file);
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
}
