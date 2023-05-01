package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.dao.ReviewRepository;
import ncl.csc8019.group12.dao.UserRepository;
import ncl.csc8019.group12.dao.eneity.Review;
import ncl.csc8019.group12.dao.eneity.User;
import ncl.csc8019.group12.utils.ConfuseUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/review")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @Resource
    private ReviewRepository reviewRepository;

    @Resource
    private UserRepository userRepository;

    @GetMapping
    public String getReviewsByPlaceId(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String placeId
    ) {
        log.info("[Review-Get] placeId:{}; clientId:{}", placeId, clientId);

        List<Review> reviews = reviewRepository.findAllByPlaceId(placeId);

        Map<Long, String> uidNickNameMap = userRepository
                .findUsersByUidIn(reviews.stream().map(Review::getUid).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(
                        User::getUid,
                        (u) -> u.getNickname() == null ? "" : u.getNickname()));

        JSONArray jsonArray = new JSONArray();
        reviews.forEach(review -> {
            String nickname = uidNickNameMap.get(review.getUid());
            review.setUid(null);
            review.setPlaceId(null);

            JSONObject jsonObject = new JSONObject(review);
            jsonObject.put("author", nickname.isEmpty() ? "anonymous" : nickname);
            jsonArray.put(jsonObject);
        });

        return jsonArray.toString();
    }

    @PostMapping
    public Boolean addReview(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @RequestBody Review review
    ) {
        log.info("[Review-Add] clientId:{}", clientId);


        if (review.getContent() == null || review.getContent().isEmpty()) {
            return false;
        }

        review.setId(null);

        try {
            review.setUid(ConfuseUtil.decryptToLongByRSA(token));
        } catch (Exception e) {
            log.info("[Review-Add] decryptToLong fail: {}", token);
            return false;
        }

        reviewRepository.save(review);
        return review.getId() != null;
    }

    @DeleteMapping("/{id}")
    public Boolean deleteReview(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @PathVariable Long id
    ) {
        log.info("[Review-Delete] id:{}; clientId:{}", id, clientId);

        Long uid;
        try {
            uid = ConfuseUtil.decryptToLongByRSA(token);
        } catch (Exception e) {
            log.info("[Review-Delete] decryptToLong fail: {}", token);
            return false;
        }
        Review review = new Review();
        review.setId(id);
        review.setUid(uid);

        if (reviewRepository.existsById(id)) {
            reviewRepository.delete(review);
            return true;
        }

        return false;
    }
}
