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

    @GetMapping
    public String getFavoritesByUid(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token
    ) throws Exception {
        Long uid = ConfuseUtil.decryptToLongByRSA(token);
        log.info("[Favorite-Get] uid :{}; clientId:{}", uid, clientId);

        JSONArray array = new JSONArray();
        favoriteRepository.findAllByUid(uid).forEach((favorite -> {
            String placeId = favorite.getPlaceId();
            JSONObject placeDetail = cacheService.getCachedResponse(placeId);
            if (placeDetail == null) {
                placeDetail = googleMapService.getPlaceDetail(placeId, clientId);
                cacheService.cacheResponse(placeDetail, placeId);
            }
            JSONObject placeDetailCopy = new JSONObject(placeDetail);
            placeDetailCopy.put("id", favorite.getId());

            array.put(placeDetailCopy);
        }));

        return array.toString();
    }

    @PostMapping
    public Boolean addFavorite(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @RequestBody Favorite favorite
    ) {
        log.info("[Favorite-Add] Favorite:{}; clientId:{}", favorite.getPlaceId(), clientId);


        if (cacheService.getCachedResponse(favorite.getPlaceId()) == null) {
            return false;
        }

        favorite.setId(null);

        try {
            favorite.setUid(ConfuseUtil.decryptToLongByRSA(token));
        } catch (Exception e) {
            log.info("[Favorite-Add] decryptToLong fail: {}", token);
            return false;
        }

        favoriteRepository.save(favorite);
        return favorite.getId() != null;
    }

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
