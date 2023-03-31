package ncl.csc8019.group12.service;

import org.springframework.stereotype.Service;

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

    //Key: request params
    //Value: the response
    private final static Map<Integer, String> RESPONSE_CACHE_STORAGE = new ConcurrentHashMap<>();

    /**
     * Store response json string to memory
     *
     * @param responseJsonString response json string
     * @param args               request params
     */
    public void cacheResponse(String responseJsonString, Object... args) {
        RESPONSE_CACHE_STORAGE.put(Objects.hash(args), responseJsonString);
    }

    /**
     * Get cached response json string
     *
     * @param args request params
     * @return null-if it doesn't have cache; else return response json with the same input last returned
     */
    public String getCachedResponse(Object... args) {
        return RESPONSE_CACHE_STORAGE.get(Objects.hash(args));
    }
}
