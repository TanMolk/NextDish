package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.dao.FavoriteRepository;
import ncl.csc8019.group12.dao.eneity.Favorite;
import ncl.csc8019.group12.service.CacheService;
import ncl.csc8019.group12.service.GoogleMapService;
import ncl.csc8019.group12.utils.ConfuseUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * FavouriteController is to record and update information on users' favourite restaurants.
 * This Controller contains the methods to query users' favourite restaurants(getFavoritesByUid()).
 *                                      to add users' favourite restaurants(addFavorite()).
 *                                      to delete users' favourite restaurants(deleteFavorite()).
 * @author Wei & Rachel & Pulei
 */
@CrossOrigin
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    private static final Logger log = LoggerFactory.getLogger(FavoriteController.class);

    @Resource
    private FavoriteRepository favoriteRepository;

    @Resource
    private CacheService cacheService;

    @Resource
    private GoogleMapService googleMapService;

    /**
     * Get Details of all places favourited by the user.
     * Must contain clientId and token.
     *
     * @param clientId    Unique identification information for this user
     * @param token       String for authentication
     * @return Data of favourite places
     */
    @GetMapping
        //Decrypt the token with the RSA algorithm above to obtain the uid
    public String getFavoritesByUid(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token
    ) throws Exception {
        Long uid = ConfuseUtil.decryptToLongByRSA(token);
        log.info("[Favorite-Get] uid :{}; clientId:{}", uid, clientId);

        // Get details process
        /*
         * Create a JSONArray to store information about all the locations that the user preferred.
         * use the uid to query all the Favorite data from the FavoriteRepository.
         * Iterate through all this Favorite data to get the value of the placeId property.
         * Use the CacheService to update all placeId's preferred by the user during the iteration.
         * If there is information about the user's preferred location in the cache, the data is retrieved via the CacheService.
         * If the cache does not contain information about the user's preferred location, the details are obtained via the Google API.
         * Finally, the data in the JSONArray is returned via toString().
         */

        JSONArray array = new JSONArray();
        favoriteRepository.findAllByUid(uid).forEach((favorite -> {
            String placeId = favorite.getPlaceId();
            JSONObject placeDetail = cacheService.getCachedResponse(placeId);
            if (placeDetail == null) {
                placeDetail = googleMapService.getPlaceDetail(placeId, clientId);
                cacheService.cacheResponse(placeDetail, placeId);
            }
            JSONObject placeDetailCopy = new JSONObject(placeDetail.toString());
            placeDetailCopy.put("id", favorite.getId());

            array.put(placeDetailCopy);
        }));

        return array.toString();
    }

    /**
     * Users add new favourite places.
     * Must contain clientId, token and favourite.
     *
     * @param clientId    Unique identification information for this user
     * @param token       String for authentication
     * @param favorite    Object for storing the user's favourite place
     * @return true(save successfully) or false(save failure)
     */
    @PostMapping
    public Boolean addFavorite(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @RequestBody Favorite favorite
    ) {
        log.info("[Favorite-Add] Favorite:{}; clientId:{}", favorite.getPlaceId(), clientId);

        /*
        * First, determine if the user's favourite place is stored in the cache.
        * Because the CacheService automatically stores any place that the user has visited.
        * SO if not, then the user has not browsed the area at all, and it is not possible to favourite the place, It is a meaningless operation.
        * so return false directly to save efficiency.
        */
        if (cacheService.getCachedResponse(favorite.getPlaceId()) == null) {
            return false;
        }

        /*
        * If the place is stored in the cache, initialize Id and Uid inside the favorite object.
        * Since the placeId is definitely already given, there is no need to initialize.
        * Finally, the user and his favourite place are stored in the favouriteRepository.
        */
        favorite.setId(null);

        try {
            favorite.setUid(ConfuseUtil.decryptToLongByRSA(token));
        } catch (Exception e) {
            log.info("[Favorite-Add] decryptToLong fail: {}", token);
            return false;
        }

        favoriteRepository.save(favorite);

        /*
        * The success of storing a favourite object is determined by whether the Id is null.
        * Because the favourite object will automatically assign a value to the Id via @GeneratedValue when object is successfully stored.
        */
        return favorite.getId() != null;
    }

    /**
     * Users delete favourite places.
     * Must contain clientId, token and id.
     * This method will automatically get the id from the URL.
     *
     * @param clientId    Unique identification information for this user
     * @param token       String for authentication
     * @param id          The id in the favourite object that the user wants to delete
     * @return true(Deleted successfully) or false(Failed to delete)
     */

        //{id} corresponds to the id in the favorite object that the user wants to delete, captured by @pathVariable.
    @DeleteMapping("/{id}")
    public Boolean deleteFavorite(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @PathVariable Long id
    ) {
        log.info("[Favorite-Delete] id:{}; clientId:{}", id, clientId);

        Long uid;
        try {
            uid = ConfuseUtil.decryptToLongByRSA(token);
        } catch (Exception e) {
            log.info("[Favorite-Delete] decryptToLong fail: {}", token);
            return false;
        }

        /*
        * Generate a new favourite object with the id and uid already obtained.
        * then delete the user's favourite entries with the favoriteRepository.delete() method.
        */
        Favorite favorite = new Favorite();
        favorite.setId(id);
        favorite.setUid(uid);

        if (favoriteRepository.existsById(id)) {
            favoriteRepository.delete(favorite);
            return true;
        }

        return false;
    }
}
